package net.datasa.test.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 리플 Entity
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "market_reply")
public class ReplyEntity {

	@Id
	@Column(name="reply_num")
	private Integer replyNum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="board_num", referencedColumnName = "board_num")
	private BoardEntity board;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id", referencedColumnName="member_id")
	private MemberEntity member;
	
	@Column(name="reply_text", nullable=false, length=500)
	private String replyText;

}
