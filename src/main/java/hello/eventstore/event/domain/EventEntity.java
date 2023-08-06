package hello.eventstore.event.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event_list")
@Entity
public class EventEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private LocalDateTime timestamp;

    @Lob
    private String payload;

    public EventEntity(EventType eventType, LocalDateTime timestamp, String payload) {
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.payload = payload;
    }
}
