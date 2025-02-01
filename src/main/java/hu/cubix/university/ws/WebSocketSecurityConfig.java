package hu.cubix.university.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {

		messages.simpSubscribeDestMatchers("/topic/chat/*").hasAnyAuthority("admin", "user");
	}

	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}

	
}
