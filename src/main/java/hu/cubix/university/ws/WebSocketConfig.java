package hu.cubix.university.ws;

import hu.cubix.university.model.Course;
import hu.cubix.university.model.Student;
import hu.cubix.university.model.Teacher;
import hu.cubix.university.repository.StudentRepository;
import hu.cubix.university.repository.TeacherRepository;
import hu.cubix.university.security.JwtAuthFilter;
import hu.cubix.university.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    private static final String CHAT_TOPIC_DEST_PREFIX = "topic/chat/";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/stomp");
        registry.addEndpoint("/api/stomp").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null) {
                    List<String> authHeaders = accessor.getNativeHeader("X-Authorization");
                    if (authHeaders != null) {
                        UsernamePasswordAuthenticationToken authentication = JwtAuthFilter.createUserDetailsFromAuthHeader(authHeaders.get(0), jwtService);
                        StompCommand command = accessor.getCommand();
                        UserDetails principal = (UserDetails) authentication.getPrincipal();
                        String username = principal.getUsername();
                        if (StompCommand.CONNECT.equals(command)) {
                            accessor.setUser(authentication);
                        } else if (StompCommand.SEND.equals(command)) {
                            ChatMessage chatMessage = (ChatMessage) message.getPayload();
                            if (!chatMessage.getUserName().equalsIgnoreCase(username)) {
                                return null;
                            }
                        } else if (StompCommand.SUBSCRIBE.equals(command)) {
                            Teacher teacher = teacherRepository.findTeacherByName(username).orElse(null);
                            Student student = studentRepository.findStudentByName(username).orElse(null);
                            if (teacher == null && student == null) {
                                return null;
                            }

                            String destination = (String) message.getHeaders().get("destination");
                            if (destination != null) {
                                if (destination.toLowerCase().contains(CHAT_TOPIC_DEST_PREFIX)) {
                                    Pattern pattern = Pattern.compile("\\d+");
                                    Matcher matcher = pattern.matcher(destination);
                                    if (matcher.find()) {
                                        int courseId = Integer.parseInt(matcher.group());

                                        Set<Course> courses;
                                        if (teacher != null) {
                                            courses = teacher.getCourses();
                                        } else {
                                            courses = student.getCourses();
                                        }
                                        if (courses.stream().filter(course -> course.getId() == courseId).toList().isEmpty()) {
                                            return null;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                return message;
            }

        });
    }
}