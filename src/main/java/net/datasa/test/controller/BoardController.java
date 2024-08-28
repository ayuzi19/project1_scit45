package net.datasa.test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.test.domain.dto.BoardDTO;
import net.datasa.test.domain.dto.MemberDTO;
import net.datasa.test.security.AuthenticatedUser;
import net.datasa.test.service.BoardService;
import net.datasa.test.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 거래 게시판 관련 콘트롤러
 */

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("board")
public class BoardController {

	private final BoardService bservice;

	
	@GetMapping("list")
	public String list() {
		return "board/list";
	}

	@GetMapping("write")
	public String write() {
		return "board/writeForm";
	}
	
	@PostMapping("save")
	public String writeSave(@ModelAttribute BoardDTO dto, @RequestParam("category") String category,@AuthenticationPrincipal AuthenticatedUser user) throws Exception {
		switch(category) {
		case "com": dto.setCategory("컴퓨터"); break;
		case "cam": dto.setCategory("카메라"); break;
		case "car": dto.setCategory("자동차"); break;
		default: throw new Exception("타입 오류"); 
		}
		dto.setMemberId(user.getUsername());
		log.debug("사용자가 입력한 보드 정보: {}", dto);
		
		bservice.save(dto);
		return "redirect:list";
	}
	
	@GetMapping("read")
	public String readBoard(@AuthenticationPrincipal AuthenticatedUser user, 
					@RequestParam("boardNum") Integer boardNum,
					Model model) {
		model.addAttribute("dto", bservice.select(boardNum));
		model.addAttribute("boardNum", boardNum);
		
		return "board/read";
	}
	
	@GetMapping("del")
	public String delBoard(@AuthenticationPrincipal AuthenticatedUser user,
					@RequestParam("boardNum") String boardnum) {
		try {
			int boardNum = Integer.parseInt(boardnum);
			bservice.del(user.getUsername(), boardNum);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:list";
	}

	@GetMapping("buy")
	public String buyBoard(@AuthenticationPrincipal AuthenticatedUser user,
			@RequestParam("boardNum") String boardnum) {
		log.debug("구매 컨트롤러 지나감");
		int boardNum = Integer.parseInt(boardnum);
		try {
			bservice.buy(user.getUsername(), boardNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:list";
	}
   
}
