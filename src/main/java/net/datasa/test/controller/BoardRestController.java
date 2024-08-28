package net.datasa.test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.test.domain.dto.BoardDTO;
import net.datasa.test.domain.dto.ReplyDTO;
import net.datasa.test.security.AuthenticatedUser;
import net.datasa.test.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 거래 게시판 Ajax 요청 처리 콘트롤러
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("board")
public class BoardRestController {

	private final BoardService bservice;
	
	@PostMapping("list")
	public List<BoardDTO> list(@RequestParam(name="category") String category,
								@RequestParam(name="searchWord", defaultValue="") String searchWord ) throws Exception{
		switch(category) {
		case "com": category = "컴퓨터"; break;
		case "cam": category = "카메라"; break;
		case "car": category = "자동차"; break;
		default: category = ""; break; 
		}
		
		List<BoardDTO> list; 
		
		if (category.isEmpty()) {
	        list = bservice.getListBySearchWord(searchWord);
	    } else {
	        list = bservice.getList(category, searchWord);
	    }
		
		log.debug("{}", list);
		return list;
	}
	
	@PostMapping("input")
	public void inputReply(@RequestParam("reply") String reply, @RequestParam("boardNum") Integer boardNum, @AuthenticationPrincipal AuthenticatedUser user) {
		bservice.reply(user.getUsername(), boardNum, reply);
	}

	@PostMapping("replyList")
	public List<ReplyDTO> listReply(@RequestParam("reply") String reply, @RequestParam("boardNum") Integer boardNum, @AuthenticationPrincipal AuthenticatedUser user) {
		return bservice.listReply(boardNum);
	}
	
  
}
