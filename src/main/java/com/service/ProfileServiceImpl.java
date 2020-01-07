package com.service;

import com.configuration.HibernateUtils;
import com.dto.ProfileDTO;
import com.entity.AccessToken;
import com.entity.Profile;
import com.service.base.BaseService;
import com.service.notification.NotificationCode;
import com.service.notification.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl extends BaseService implements ProfileService
{

//    Logger LOGGER = Logger.getLogger(ProfileServiceImpl.class);

    @Autowired AccessTokenService accessTokenService;
    @Autowired DozerService dozerService;
    @Autowired HibernateUtils hibernateUtils;

    private static String GET_BY_ID = "FROM " + Profile.class.getName() + " as p WHERE p.user.id = :id";

    @Override
    public ResponseResult updateProfile(String accessToken, ProfileDTO profileDTO) {
        if(StringUtils.isEmpty(accessToken)){
            return ResponseResult.failed(NotificationCode.ErrorCode.INVALID_ACCESS_TOKEN);
        }
        AccessToken accessTokenObj = accessTokenService.getByAccessToken(accessToken);
        if(accessTokenObj == null || accessTokenObj.getUser() == null){
            return ResponseResult.failed(NotificationCode.ErrorCode.WRONG_ACCESS_TOKEN);
        }
        Profile profile = getProfileByUserID(accessTokenObj.getUser().getId());
        if(profile == null){
            profile = dozerService.getDozer().map(profileDTO, Profile.class);
            profile.setUser(accessTokenObj.getUser());
            save(profile);
        }else{
            profile = dozerService.getDozer().map(profileDTO, Profile.class);
            profile.setUser(accessTokenObj.getUser());
            update(profile);
        }
        return ResponseResult.isSuccess(NotificationCode.SuccessCode.SUCCESS, profile);
    }

    public Profile getProfileByUserID(int userID){
        Session session = hibernateUtils.getSessionFactory().openSession();
        try {
            Query query = session.createQuery(GET_BY_ID);
            query.setParameter("id", userID);
            List<Profile> profiles = query.getResultList();
            if(profiles != null && profiles.size() > 0){
                return profiles.get(0);
            }
        }finally {
            session.close();
        }
        return null;
    }

}
