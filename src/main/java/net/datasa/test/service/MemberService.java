package net.datasa.test.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import net.datasa.test.domain.dto.MemberDTO;
import net.datasa.test.domain.entity.MemberEntity;
import net.datasa.test.repository.MemberRepository;

/**
 * 회원정보 서비스
 */
@RequiredArgsConstructor
@Service
@Transactional
@Builder
public class MemberService {

    //WebSecurityConfig에서 생성한 암호화 인코더
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository mrepository;
    
    /**
     * 회원 가입 메서드
     * @param dto 사용자가 입력한 정보
     */
    public void join(MemberDTO dto) {
    	MemberEntity mentity = MemberEntity.builder()
    			.memberId(dto.getMemberId())
    			.memberPw(passwordEncoder.encode(dto.getMemberPw()))
    			.memberName(dto.getMemberName())
    			.phone(dto.getPhone())
    			.build();
    	mrepository.save(mentity);
    }
    

}
