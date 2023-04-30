package com.staticvoid.invitecode.service;

import com.staticvoid.invitecode.domain.InviteCode;
import com.staticvoid.invitecode.repository.InviteCodeRepository;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.util.InviteCodeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class InviteCodeService {

    private final InviteCodeRepository inviteCodeRepository;

    public boolean validateCode(String code) {
        InviteCode inviteCode = inviteCodeRepository.findByCode(code).orElse(null);
        return inviteCode != null && !inviteCode.isUsed();
    }

    public void useCode(String code, ApplicationUser user) {
        InviteCode inviteCode = inviteCodeRepository.findByCode(code).orElse(null);
        if (inviteCode != null) {
            inviteCode.setUsed(true);
            inviteCode.setUser(user);
            inviteCodeRepository.save(inviteCode);
        }
    }

    public InviteCode createCode(String code) {
        InviteCode inviteCode = new InviteCode();
        inviteCode.setCode(code);
        inviteCode.setUsed(false);
        return inviteCodeRepository.save(inviteCode);
    }

    public List<InviteCode> generateCodes(int numberOfCodes) {
        List<InviteCode> inviteCodes = new ArrayList<>();
        String [] codes = InviteCodeUtil.generateCodes(numberOfCodes);
        Arrays.asList(codes).forEach(code -> inviteCodes.add(createCode(code)));
        return inviteCodes;
    }

}
