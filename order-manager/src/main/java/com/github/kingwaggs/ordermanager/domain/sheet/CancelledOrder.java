package com.github.kingwaggs.ordermanager.domain.sheet;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cancelled_order")
@Getter @Setter
@ToString
@Builder(access = AccessLevel.PRIVATE)
@DynamicUpdate
@AllArgsConstructor @NoArgsConstructor
public class CancelledOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_sheet_id", nullable = false)
    @ToString.Exclude
    private Order order;

    @Column(name = "cancel_id")
    private String cancelId;

    @Column(name = "purchase_agency_cancel_id")
    private String purchaseAgencyCancelId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "cancel_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime cancelDate;

    public static CancelledOrder createFrom(Order order, String cancelId) {
        return CancelledOrder.builder()
                .order(order)
                .cancelId(cancelId)
                .build();
    }

    public static CancelledOrder createFrom(Order order, String cancelId, LocalDateTime cancelDate) {
        return CancelledOrder.builder()
                .order(order)
                .cancelId(cancelId)
                .cancelDate(cancelDate)
                .build();
    }

}

