package study._01_06_querydsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study._01_06_querydsl.dto.MemberSearchCondition;
import study._01_06_querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom  {
    List<MemberTeamDto> search(MemberSearchCondition condition);
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable);
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);

}
