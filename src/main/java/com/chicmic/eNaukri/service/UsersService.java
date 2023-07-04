package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.SocialLinkDto;
import com.chicmic.eNaukri.Dto.UserProfileDto;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import com.chicmic.eNaukri.util.FileUploadUtil;
import com.chicmic.eNaukri.util.JwtUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {
    private final UsersRepo usersRepo;
    private final JavaMailSender javaMailSender;
    private final UserProfileRepo userProfileRepo;
    private final SkillsRepo skillsRepo;
    private final PreferenceRepo preferenceRepo;
    private final SocialLinkRepo socialLinkRepo;


    public Users getUserByEmail(String email) {
        return usersRepo.findByEmail(email);
    }
    public Users getUserByUuid(String uuid) { return usersRepo.findByUuid(uuid); }

    public Users getUserById(Long userId){return usersRepo.findByUserId(userId);}


    public Users register(Users newUser) {
        String uuid= UUID.randomUUID().toString();
        newUser.setUuid(uuid);
        newUser.setPassword(passwordEncoder().encode(newUser.getPassword()));
        usersRepo.save(newUser);
        sendVerificationLink(newUser.getEmail());
        return newUser;
    }
    public void sendVerificationLink(String email){
        Users user = usersRepo.findByEmail(email);
        if(user==null){
            throw new ApiException(HttpStatus.NOT_FOUND,"No user by this email, consider signing up");
        }
        String token = JwtUtils.createJwtToken(user.getUuid());
        String link = "http://localhost:8081/eNaukri/verify/"+token+"/"+user.getUuid();
        String to= user.getEmail();
        String subject="eNaukri job portal - Verify your account";
        String body="Click the link to verify your account " +link;
        sendEmail(to,subject,body);
    }
    public void verifyUserAccount(String token, String uuid){
        String decodedToken = JwtUtils.verifyJwtToken(token);
        Users user= usersRepo.findByUuid(uuid);
        if(decodedToken==uuid){
            user.setVerified(true);
        }
        else{
            throw new ApiException(HttpStatus.FORBIDDEN,"The token has expired consider making a new request");
        }
    }

    @Async public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("harmanjyot.singh@chicmic.co.in");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
//        javaMailSender.send(message);
    }

    public void updateUser(@Valid UsersDto user, @RequestParam MultipartFile imgFile,
                           @RequestParam MultipartFile resumeFile) throws IOException {
        Users existingUser=usersRepo.findByEmail(user.getEmail());
        ObjectMapper mapper = CustomObjectMapper.createObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.updateValue(existingUser,user);
        getUserProfile(existingUser).setPpPath(FileUploadUtil.imageUpload(imgFile));
        getUserProfile(existingUser).setCvPath(FileUploadUtil.resumeUpload(resumeFile));
        existingUser.setUpdatedAt(LocalDateTime.now());
        usersRepo.save(existingUser);
    }
    public UserProfile createProfile(UserProfileDto dto, Principal principal, MultipartFile imgFile, SocialLinkDto socialLinkDto)
            throws IOException{
        Users user = usersRepo.findByEmail(principal.getName());
        UserProfile userProfile= CustomObjectMapper.convertDtoToObject(dto, UserProfile.class);
        userProfile.setUsers(user);
        userProfile.setPpPath(FileUploadUtil.imageUpload(imgFile));
        List<UserSkills> userSkillsList=new ArrayList<>();
        for (Long skillId : dto.getSkillsList()) {
            Skills skill = skillsRepo.findById(skillId).orElse(null);
            UserSkills userSkills = UserSkills.builder().skills(skill).userProfile(userProfile).build();
            if (skill != null) {
                userSkillsList.add(userSkills);
            }
        }
        for(Education ed:userProfile.getEducationList()){
            ed.setEdUser(userProfile);
        }
        for(Experience ex:userProfile.getExperienceList()){
            ex.setExpUser(userProfile);
        }
        SocialLink socialLink=CustomObjectMapper.convertDtoToObject(dto, SocialLink.class);
        socialLink.setUser(user);
        user.setSocialLink(socialLink);
        usersRepo.save(user);
        socialLinkRepo.save(socialLink);
        userProfile.setUserSkillsList(userSkillsList);
        userProfile.setUsers(user);
        userProfileRepo.save(userProfile);
        return userProfile;
    }
    public UserProfile createProfile(UserProfileDto dto, Principal principal, MultipartFile imgFile)
            throws IOException{
        Users user = usersRepo.findByEmail(principal.getName());
        UserProfile userProfile= CustomObjectMapper.convertDtoToObject(dto, UserProfile.class);
        userProfile.setUsers(user);
        userProfile.setPpPath(FileUploadUtil.imageUpload(imgFile));
        List<UserSkills> userSkillsList=new ArrayList<>();
        for (Long skillId : dto.getSkillsList()) {
            Skills skill = skillsRepo.findById(skillId).orElse(null);
            UserSkills userSkills = UserSkills.builder().skills(skill).userProfile(userProfile).build();
            if (skill != null) {
                userSkillsList.add(userSkills);
            }
        }
        for(Education ed:userProfile.getEducationList()){
            ed.setEdUser(userProfile);
        }
        for(Experience ex:userProfile.getExperienceList()){
            ex.setExpUser(userProfile);
        }
        userProfile.getPreference().setUserPreferences(userProfile);
        userProfile.setUserSkillsList(userSkillsList);
        userProfile.setUsers(user);
        userProfileRepo.save(userProfile);
        return userProfile;
    }
    public Preference createPreferences(Principal principal, Preference preference){
        Users user=usersRepo.findByEmail(principal.getName());
        Preference preference1=preferenceRepo.save(preference);
        preference1.setUserPreferences(getUserProfile(user));
        return preference1;
    }
    public Preference updatePreferences(Principal principal, Preference preference){
        Users user=usersRepo.findByEmail(principal.getName());
        Preference oldPreferences=getUserProfile(user).getPreference();
        oldPreferences=preference;
        return oldPreferences;
    }
    public UserProfile getUserProfile(Users users) {
        return userProfileRepo.findUserProfileByUsers(users);
    }
    public List<UserProfile> searchUser(String query) {
        return userProfileRepo.findByQuery(query);
    }
}
