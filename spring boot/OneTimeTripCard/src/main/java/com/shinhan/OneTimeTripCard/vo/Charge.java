package com.shinhan.OneTimeTripCard.vo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Charge {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // pk
	private Long chargerId; // 충전자 아이디
	private String cardNo; // 카드 번호
	private String currency; // 통화($)
	private Double rate;  // 환율(충전 당시)
	private Double amount; // 충전 금액(자국 통화)
	private Integer amountWon; // 충전 금액(원화)
	private Double commissionPct; // 수수료
	private LocalDateTime createdAt;
}
