package net.datasa.test.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.test.domain.entity.MemberEntity;
import net.datasa.test.repository.MemberRepository;

/**
 * 사용자 인증 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticatedUserDetailsService implements UserDetailsService {

	// 이미 만들어진 암호화 객체를 사용 > 비밀번호 암호화
	private final BCryptPasswordEncoder passwordEncoder;
    // 로그인 기능 만들기 위해 불러옴
	private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    	log.info("로그인 시도 : {}", username);
    	
    // 전달받은 사용자 ID, username으로 DB에서 사용자 정보 조회
	MemberEntity mentity = memberRepository.findById(username)
			.orElseThrow(()-> new UsernameNotFoundException("존재하지 않는 계정입니다"));
    
	// 있으면 조회된 정보로 객체 생성해 리턴
	AuthenticatedUser user = AuthenticatedUser.builder()
			.memberId(username)
			.memberPw(mentity.getMemberPw())
			.memberName(mentity.getMemberName())
			.phone(mentity.getPhone())
			.build();
	log.debug("인증정보:{}", user);

	return user;
    }
}
