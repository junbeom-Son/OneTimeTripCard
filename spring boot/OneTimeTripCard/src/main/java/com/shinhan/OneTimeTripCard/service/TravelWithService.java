package com.shinhan.OneTimeTripCard.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shinhan.OneTimeTripCard.repository.UserCardRepository;
import com.shinhan.OneTimeTripCard.vo.Grade;
import com.shinhan.OneTimeTripCard.vo.User;
import com.shinhan.OneTimeTripCard.vo.UserCard;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelWithService {

	private final UserService userService;
	private final GradeService gradeService;
	private final UserCardRepository userCardRepository;
	
	/**
	 * 카드를 만드는 사람이 발급
	 * 초대한 이메일 목록을 기반으로 각각 userCard를 만듬(조회용)
	 * 등급은 일반 등급
	 * 매니저는 매니저 아이디를 기반으로 카드 등록
	 * @param managerId
	 * @param nickName
	 * @param invitedEmails
	 * @param isDefault
	 * @return
	 */
	public UserCard register(Long managerId, String nickName, List<String> invitedEmails, Boolean isDefault) {
		List<UserCard> userCards = new ArrayList<>();
		User manager = userService.findById(managerId);
		Grade basicGrade = gradeService.getGradeByName("일반");
		Long travelWithId = userCardRepository.getNextGroupSequence();
		UserCard managerCard = createTravelWithCard(manager, manager, nickName, basicGrade, travelWithId, isDefault);
		userCards.add(managerCard);
		for (String invitedEmail : invitedEmails) {
			User user = userService.findByEmail(invitedEmail);
			if (user == null) {
				continue;
			}
			UserCard userCard = createTravelWithCard(user, manager, nickName, basicGrade, travelWithId, isDefault);
			
			userCards.add(userCard);
		}
		List<UserCard> savedUserCards = (List<UserCard>) userCardRepository.saveAll(userCards);
		for (UserCard savedUserCard : savedUserCards) {
			if (savedUserCard.getUser().getId() == managerId) {
				managerCard = savedUserCard;
			}
		}
		return managerCard;
	}
	
	/**
	 * managerId를 기반으로 TravelWithCard(공용카드) 생성
	 * @param user
	 * @param manager
	 * @param basicGrade
	 * @param travelWithId (그룹 카드 id)
	 * @return
	 */
	private UserCard createTravelWithCard(User user, User manager, String nickName, Grade basicGrade, Long travelWithId, Boolean isDefault) {
		UserCard userCard = UserCard.builder()
				.user(user)
				.manager(manager)
				.nickName(nickName)
				.grade(basicGrade)
				.isGroup(true)
				.travelWithId(travelWithId)
				.isDefault(isDefault)
				.build();
		return userCard;
	}

	public List<UserCard> getAllTravelWithCards(Long userId) {
		return userCardRepository.findByUser_IdAndIsGroup(userId, true);
	}
	
	public List<User> getAllUsersInTravelWithGroup(Long travelWithId) {
		return userCardRepository.getUsersByTravelWithId(travelWithId);
	}
}
