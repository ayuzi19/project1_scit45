package net.datasa.test.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.test.domain.dto.BoardDTO;
import net.datasa.test.domain.dto.ReplyDTO;
import net.datasa.test.domain.entity.BoardEntity;
import net.datasa.test.domain.entity.MemberEntity;
import net.datasa.test.domain.entity.ReplyEntity;
import net.datasa.test.repository.BoardRepository;
import net.datasa.test.repository.MemberRepository;
import net.datasa.test.repository.ReplyRepository;

/**
 * 게시판 서비스
 */
@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class BoardService {

	private final MemberRepository mrepository;
	private final BoardRepository brepository;
	private final ReplyRepository rrepository;

	public List<BoardDTO> getList(String category, String searchWord){
		
		log.debug("받아온 값 - 카테고리: {}, - 검색어: {}", category, searchWord);
		
		List<BoardEntity> entityList = new ArrayList<>();
		List<BoardDTO> dtoList = new ArrayList<>();
		
		entityList = brepository.findByCategoryAndTitleContaining(category, searchWord);
		
		for(BoardEntity entity : entityList) {
			BoardDTO dto = BoardDTO.builder()
					.boardNum(entity.getBoardNum())
					.memberId(entity.getMember().getMemberId())
					.category(entity.getCategory())
					.title(entity.getTitle())
					.contents(entity.getContents())
					.price(entity.getPrice())
					.soldout(entity.getSoldout())
					.inputDate(entity.getInputDate())
					//.buyerId(entity.getBuyer().getMemberId())
					.build();
					
			dtoList.add(dto);
		}
		
		return dtoList;
	}
	
	public List<BoardDTO> getListBySearchWord(@RequestParam("searchWord") String searchWord){
		log.debug("받아온 값 - 검색어: {}", searchWord);
		
		List<BoardEntity> entityList = new ArrayList<>();
		List<BoardDTO> dtoList = new ArrayList<>();
		
		entityList = brepository.findByTitleContaining(searchWord);
		
		for(BoardEntity entity : entityList) {
			BoardDTO dto = BoardDTO.builder()
					.boardNum(entity.getBoardNum())
					.memberId(entity.getMember().getMemberId())
					.category(entity.getCategory())
					.title(entity.getTitle())
					.contents(entity.getContents())
					.price(entity.getPrice())
					.soldout(entity.getSoldout())
					.inputDate(entity.getInputDate())
					.build();	
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	public void save(BoardDTO dto) {
	     //로그인한 사용자의 아이디로 회원정보를 조회한다.
        MemberEntity memberEntity = mrepository.findById(dto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("회원아이디가 없습니다."));
			
        // 엔티티 객체 생성
        BoardEntity bentity = BoardEntity.builder()
        		.member(memberEntity)
        		.category(dto.getCategory())
        		.soldout(false)
        		.title(dto.getTitle())
        		.contents(dto.getContents())
        		.price(dto.getPrice())
				.build();
        
        brepository.save(bentity);
        log.debug("저장되는 글 엔티티: {}", bentity);
	}

	public BoardDTO select(Integer boardNum) {
		BoardEntity entity = brepository.findById(boardNum)
				.orElseThrow(()-> new EntityNotFoundException("존재하지 않는 게시글"));
		
		BoardDTO dto = BoardDTO.builder()
				.boardNum(entity.getBoardNum())
				.memberId(entity.getMember().getMemberId())
				.category(entity.getCategory())
				.title(entity.getTitle())
				.contents(entity.getContents())
				.price(entity.getPrice())
				.soldout(entity.getSoldout())
				.inputDate(entity.getInputDate())
				.build();
		 if (entity.getBuyer() != null && entity.getBuyer().getMemberId() != null) {
		        dto.setBuyerId(entity.getBuyer().getMemberId());
		    }
		return dto;
	}
	
	public void del(String memberId, Integer boardNum) throws IllegalAccessException {
		BoardEntity bentity = brepository.findById(boardNum)
				.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글"));
		
		MemberEntity mentity = mrepository.findById(memberId)
				.orElseThrow(()-> new EntityNotFoundException("존재하지 않는 사용자"));
		
		if(bentity.getMember().getMemberId().equals(mentity.getMemberId())) {
				brepository.delete(bentity);
		}
		else {
			throw new IllegalAccessException("본인이 쓴 글만 삭제 가능");
		}
	}
	
	public void buy(String memberId, Integer boardNum) throws IllegalAccessException {
		BoardEntity bentity = brepository.findById(boardNum)
				.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글"));
		
		MemberEntity mentity = mrepository.findById(memberId)
				.orElseThrow(()-> new EntityNotFoundException("존재하지 않는 사용자"));
		
		if(!bentity.getMember().getMemberId().equals(mentity.getMemberId())) {
				bentity.setSoldout(true);
				bentity.setBuyer(mentity);
				
		}
		else {
			throw new IllegalAccessException("본인이 쓴 글만 삭제 가능");
		}
	}
	
	/**
	 * 댓글 저장
	 * @param memberId
	 * @param boardNum
	 * @param reply
	 */
	public void reply(String memberId, Integer boardNum, String reply){
		BoardEntity bentity = brepository.findById(boardNum)
				.orElseThrow(()-> new EntityNotFoundException("존재하지 않는 게시글"));
		
		MemberEntity mentity = mrepository.findById(memberId)
				.orElseThrow(()-> new EntityNotFoundException("존재하지 않는 사용자"));
		
		ReplyEntity rentity = ReplyEntity.builder()
				.member(mentity)
				.board(bentity)
				.replyText(reply)
				.build();
		rrepository.save(rentity);
		
	}
	
	/**
	 * 게시글 별 댓글 출력
	 * @return
	 */
	public List<ReplyDTO> listReply(Integer boardNum){
		List<ReplyEntity> entityList = rrepository.findByBoard_BoardNum(boardNum);
		List<ReplyDTO> dtoList = new ArrayList<>();
		for(ReplyEntity entity:entityList) {
			ReplyDTO dto = ReplyDTO.builder()
					.memberId(entity.getMember().getMemberId())
					.boardNum(entity.getBoard().getBoardNum())
					.replyText(entity.getReplyText())
					.build();
			dtoList.add(dto);
		}
		
		return dtoList;
	}
}
