package com.ivan.alkemybackendchallenge;

import com.ivan.alkemybackendchallenge.feature.dto.data.MediaCharacterDto;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaGenreDto;
import com.ivan.alkemybackendchallenge.feature.dto.data.MediaWorkDto;
import com.ivan.alkemybackendchallenge.feature.service.MediaCharacterService;
import com.ivan.alkemybackendchallenge.feature.service.MediaGenreService;
import com.ivan.alkemybackendchallenge.feature.service.MediaWorkService;
import com.ivan.alkemybackendchallenge.security.dto.data.AppUserDto;
import com.ivan.alkemybackendchallenge.security.dto.data.RoleDto;
import com.ivan.alkemybackendchallenge.security.service.AppUserService;
import com.ivan.alkemybackendchallenge.security.service.RoleService;
import com.ivan.alkemybackendchallenge.security.utility.SecurityConstants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class AlkemyBackendChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlkemyBackendChallengeApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AppUserService appUserService,
						  RoleService roleService,
						  MediaCharacterService mediaCharacterService,
						  MediaGenreService mediaGenreService,
						  MediaWorkService mediaWorkService) {

		return args -> {
			this.createSomeTestSecurityEntities(appUserService, roleService);
			this.createSomeTestFeatureEntities(mediaCharacterService, mediaGenreService, mediaWorkService);
		};

	}

	private void createSomeTestSecurityEntities(AppUserService appUserService, RoleService roleService) {
		roleService.saveRole(new RoleDto(SecurityConstants.DEFAULT_USER_ROLE));
		roleService.saveRole(new RoleDto(SecurityConstants.DEFAULT_ADMIN_ROLE));

		appUserService.saveAppUser(new AppUserDto(SecurityConstants.TEST_ADMIN_USERNAME, SecurityConstants.TEST_ADMIN_PASSWORD).setAlias("@Admin"));
		appUserService.saveAppUser(new AppUserDto(SecurityConstants.TEST_USER_USERNAME, SecurityConstants.TEST_USER_PASSWORD).setAlias("@Some_User"));

		appUserService.addRoleToAppUser(SecurityConstants.TEST_ADMIN_USERNAME, SecurityConstants.DEFAULT_ADMIN_ROLE);
	}

	private void createSomeTestFeatureEntities(MediaCharacterService mediaCharacterService,
											   MediaGenreService mediaGenreService,
											   MediaWorkService mediaWorkService) {

		MediaCharacterDto mickey = mediaCharacterService.createMediaCharacter(new MediaCharacterDto(
				null,
				"https://upload.wikimedia.org/wikipedia/en/thumb/d/d4/Mickey_Mouse.png/220px-Mickey_Mouse.png",
				"Mickey Mouse",
				93,
				10d,
				"Mickey Mouse is an American animated cartoon character co-created in 1928 by Walt Disney, who originally voiced the character, and Ub Iwerks. The longtime mascot of The Walt Disney Company, Mickey is an anthropomorphic mouse who typically wears red shorts, large yellow shoes, and white gloves."
		)); // Mickey was saved in the db and now this dto has an id.
		MediaCharacterDto goofy = mediaCharacterService.createMediaCharacter(new MediaCharacterDto(
				null,
				"https://upload.wikimedia.org/wikipedia/en/thumb/5/50/Goofy_Duckipedia.png/220px-Goofy_Duckipedia.png",
				"Goofy",
				90,
				40d,
				"Goofy is a cartoon character created by The Walt Disney Company. He is a tall, anthropomorphic dog who typically wears a turtle neck and vest, with pants, shoes, white gloves, and a tall hat originally designed as a rumpled fedora."
		)); // Goofy was saved in the db and now this dto has an id. This character will remain unattached to any media work, so it can be deleted.
		MediaCharacterDto pluto = new MediaCharacterDto(
				null,
				"https://upload.wikimedia.org/wikipedia/en/thumb/b/b2/Pluto_%28Disney%29_transparent.png/220px-Pluto_%28Disney%29_transparent.png",
				"Pluto",
				null,
				null,
				null
		); // Pluto is not yet stored in the db, id is null.
		MediaGenreDto comedyGenre = mediaGenreService.createGenre(new MediaGenreDto(
				null,
				"Comedy",
				null,
				null
		)); // The Comedy genre was saved in the db and now this dto has an id.
		MediaGenreDto animationGenre = new MediaGenreDto(
				null,
				"Animation",
				null,
				null
		); // The Animation genre is not yet stored in the db, id is null.
		mediaWorkService.createMediaWork(new MediaWorkDto(
				"movie",
				"https://upload.wikimedia.org/wikipedia/en/thumb/b/bb/Disney%27s_The_Prince_and_the_Pauper_%281990%29.jpg/220px-Disney%27s_The_Prince_and_the_Pauper_%281990%29.jpg",
				"The Prince and the Pauper",
				"1990-11-16",
				3.5,
				Arrays.asList(mickey, pluto),
				Arrays.asList(comedyGenre, animationGenre)
		));
		// Create a media work with only elements that already exist
		mediaWorkService.createMediaWork(new MediaWorkDto(
				"movie",
				"https://upload.wikimedia.org/wikipedia/en/thumb/1/12/Fantasia-poster-1940.jpg/220px-Fantasia-poster-1940.jpg",
				"Fantasia",
				"1940-11-13",
				5.0,
				Arrays.asList(mickey),
				Arrays.asList(
						mediaGenreService.getMediaGenre("Animation"),
						mediaGenreService.getMediaGenre("Comedy")
				)
		));
	}

}
