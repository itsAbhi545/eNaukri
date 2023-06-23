package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.CompanyCategories;
import com.stripe.model.issuing.Cardholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyCategoriesRepo extends JpaRepository<CompanyCategories, Long> {

    @Query(
            "Select Company From CompanyCategories cc where  (cc.company in ?1 And cc.categories in ?2)"
    )
    List<Company> searchCompanyAndCategory(List<Company> companyList, List<Long> categoryList);
}
