package suzzingv.suzzingv.rockstar.domain.schedule_request.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import suzzingv.suzzingv.rockstar.domain.schedule_request.domain.enums.RequestStatus;

@Entity
@Table(name = "schedule_request_assignees")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScheduleRequestAssignees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long requestId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Builder
    private ScheduleRequestAssignees(Long requestId, Long userId, RequestStatus status) {
        this.requestId = requestId;
        this.userId = userId;
        this.status = status;
    }

    public void completeRequest() {
        this.status = RequestStatus.COMPLETED;
    }
}
