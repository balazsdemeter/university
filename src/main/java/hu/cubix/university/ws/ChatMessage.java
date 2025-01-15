package hu.cubix.university.ws;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ChatMessage {
	private String message;
	private String userName;
	private OffsetDateTime timestamp;
}