package net.datasa.test.domain.dto;

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
 * 판매글 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

	private Integer boardNum;
	private String memberId;
	private String category;
	private String title;
	private String contents;
	private Integer price;
	private Boolean soldout;
	private String buyerId;
	private LocalDateTime inputDate;

}
