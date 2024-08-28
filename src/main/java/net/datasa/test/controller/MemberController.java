package net.datasa.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.test.domain.dto.MemberDTO;
import net.datasa.test.service.MemberService;

/**
 * 회원 관련 콘트롤러
 */

@Slf4j
@Data
@RequiredArgsConstructor
@Controller
@RequestMapping("member")
public class MemberController {

	private final MemberService mservice;
	
   @GetMapping("join")
   public String joinForm() {
	   return "member/joinForm";
   }
   
   @PostMapping("join")
   public String join(@ModelAttribute MemberDTO dto) {
	   log.debug("입력된 값:{}", dto);
	   mservice.join(dto);
	   return "redirect:/";
   }
   
   /**
	 * 로그인 버튼 누르면 
	 * @return 로그인폼 html로 포워딩
	 */
	@GetMapping("login")
	public String login() {
		return "member/loginForm";
	}
	
}
