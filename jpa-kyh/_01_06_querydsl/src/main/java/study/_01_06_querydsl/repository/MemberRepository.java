package study._01_06_querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study._01_06_querydsl.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{
    List<Member> findByUsername(String username);
}
