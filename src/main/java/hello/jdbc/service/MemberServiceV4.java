package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * 예외 누수 문제 해결
 * SQLException 제거
 *
 * MemberRepository 인터페이스 의존
 */
public class MemberServiceV4 {

    private final MemberRepository memberRepository;

    public MemberServiceV4(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void accountTransfer(String fromId, String toId, int money)  {
                bizLogic(toId, fromId, money);
    }

    private void bizLogic(String toId, String fromId, int money) {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney()- money);
        validation(toMember);
        memberRepository.update(toId,toMember.getMoney()+ money);
    }
    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("memberEX")) {
            throw new IllegalStateException();
        }
    }
}
