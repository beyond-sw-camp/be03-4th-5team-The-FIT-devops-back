package com.example.TheFit.common;

import com.example.TheFit.user.dto.UserIdPassword;
import com.example.TheFit.user.entity.Gender;
import com.example.TheFit.user.entity.Role;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.member.repository.MemberRepository;
import com.example.TheFit.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    @Autowired
    public InitialDataLoader(UserRepository userRepository, MemberRepository memberRepository) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception{
        if(userRepository.findByEmail("admin@test.com").isEmpty()){
            Member member = Member.builder()
                    .name("admin")
                    .email("admin@test.com")
                    .password("1234")
                    .role(Role.ADMIN)
                    .gender(Gender.FEMALE)
                    .phoneNumber("01089842757")
                    .build();
            userRepository.save(new UserIdPassword("admin@test.com","1234","admin",Role.ADMIN));
            memberRepository.save(member);
        }
    }

}
