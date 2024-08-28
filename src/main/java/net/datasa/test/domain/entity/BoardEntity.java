package net.datasa.test.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 판매글 Entity
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "market_board")
public class BoardEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment로 자동으로 생성될 시
	@Column(name="board_num", nullable=false)
	private Integer boardNum;
	
	@ManyToOne(fetch = FetchType.LAZY)	// 1:다 관계 -> member테이블과 board테이블 정보 연결
	@JoinColumn(name="member_id", referencedColumnName = "member_id")
	private MemberEntity member;
	
	@Column(name="category", length=50, nullable=false)
	private String category;
	
	@Column(name="title", length=200, nullable=false)
	private String title;
	
	@Column(name="contents", length=2000, nullable=false)
	private String contents;
	
	@Column(name="price", nullable=false)
	@Builder.Default
	private Integer price = 0;
	
	@Column(name="soldout", columnDefinition="tinyint(1) default 0 check (soldout in(0, 1))")
	private Boolean soldout;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="buyer_id", columnDefinition="varchar(20)", referencedColumnName="member_id")
	private MemberEntity buyer;
	
	@CreatedDate
	@Column(name="input_date", columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
	LocalDateTime inputDate;
}
