package Prototype;

import java.lang.reflect.Array;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
	private static final int FRAME_COLS_PLAYER = 4;
	private static final int FRAME_ROWS_PLAYER = 20;
	private static final int FRAME_COLS_OBJECT = 3;
	private static final int FRAME_COLS_ROBOT = 15;
	private static final int FRAME_ROWS_ROBOT = 13;


	public static final int ANIMATION_STILL = 0;
	public static final int ANIMATION_INTERACT = 19;
	public static final int ANIMATION_INTERACT_ROBOT = 0;
	public static final int ANIMATION_DRAGGED = 17;
	public static final int ANIMATION_DIE = 18;
	public static final int ANIMATION_ATTACK = 9;
	public static final int ANIMATION_ATTACK_GRABBING = 10;
	public static final int ANIMATION_GRAB = 11;
	public static final int ANIMATION_RUSH = 12;

	public static final float SOUND_INTERVAL_RUNNING = 0.5f;
	public static final float SOUND_INTERVAL_DODGE = 1f;
	public static final float SOUND_INTERVAL_ATTACK = 0.6f;
	public static final float SOUND_INTERVAL_RUSH = 0.2f;
	public static final float AUDIBILITY_RANGE = 600f;

	TextureRegion baseTexture;

	final Texture mainAreaTexture;
	final Texture pathToEntranceAreaTexture;
	final Texture westPassageAreaTexture;
	final Texture westHallwayAreaTexture;
	final Texture southPassageAreaTexture;
	final Texture southHallwayAreaTexture;
	final Texture testRoomAreaTexture;
	final Texture developmentRoomAreaTexture;
	final Texture directorOfficeAreaTexture;
	final Texture serverRoomAreaTexture;
	final Texture officeAreaTexture;
	final Texture pathToExitAreaTexture;

	final Texture boxClosedTexture;
	final Texture boxOpenedTexture;
	final Texture cardKeyTexture;
	final Texture projectileTexture;

	final Texture robotSpriteSheet;
	final Texture player1SpriteSheet;
	final Texture player2SpriteSheet;
	final Texture player3SpriteSheet;
	final Texture player4SpriteSheet;
	final Texture gateObjectSpriteSheet;
	final Texture doorObjectSpriteSheet;
	final Texture doorObjectRotatedSpriteSheet;
	//final Texture gateObjectRotatedSpriteSheet;

	final Animation<TextureRegion>[] robotAnimations;
	final Animation<TextureRegion>[] player1Animations;
	final Animation<TextureRegion>[] player2Animations;
	final Animation<TextureRegion>[] player3Animations;
	final Animation<TextureRegion>[] player4Animations;

	final Animation<TextureRegion> gateAnimation;
	final Animation<TextureRegion> doorAnimation;
	final Animation<TextureRegion> doorAnimationRotated;
	//final Animation<TextureRegion> gateAnimationRotated;

	//final Texture remainTimeBoxClosedTexture;
	final Texture interactableMark;
	final Texture portraitNormalTexture;
	final Texture portraitKeyTexture;
	final Texture portraitKilledTexture;
	final Texture portraitDisconnectedTexture;
	final Texture portraitEscapedTexture;
	final Texture portraitsTexture;
	final TextureRegionDrawable portraitNormalDrawable;
	final TextureRegionDrawable portraitKeyDrawable;
	final TextureRegionDrawable portraitKilledDrawable;
	final TextureRegionDrawable portraitDisconnectedDrawable;
	final TextureRegionDrawable portraitEscapedDrawable;
	Skin skin;

	final Texture resultWindowTexture;

	final Texture introMatchInfoTexture;
	final Texture introBackgroundTexture;
	final Texture introRobotSpriteSheet;
	final Animation<TextureRegion> introRobotAnimation;

	final Sound introRobotMalfunction;
	final Sound introRobotVoice;
	final Sound introAlarm;
	final Sound introBackground;

	final Texture mainMenuBackgroundTexture;

	final Music mainMenuMusic;
	final Sound click;

	final Sound resultWindowDown;
	final Sound resultPop;
	final Sound resultMessage;

	//Sound[] humanSounds;

	Assets() {
		//robotAnimations = (Animation<TextureRegion>[]) Array.newInstance(Animation.class, FRAME_ROWS_ROBOT);
		robotAnimations = (Animation<TextureRegion>[])new Animation[FRAME_ROWS_ROBOT];
		player1Animations = (Animation<TextureRegion>[])new Animation[FRAME_ROWS_PLAYER];
		player2Animations = (Animation<TextureRegion>[])new Animation[FRAME_ROWS_PLAYER];
		player3Animations = (Animation<TextureRegion>[])new Animation[FRAME_ROWS_PLAYER];
		player4Animations = (Animation<TextureRegion>[])new Animation[FRAME_ROWS_PLAYER];

		//base texture
		Texture baseTileTexture = new Texture(Gdx.files.internal("tile_base.png"));
		baseTileTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		baseTexture = new TextureRegion(baseTileTexture);
		//area textures
		mainAreaTexture = new Texture(Gdx.files.internal("area_main.png"));
		pathToEntranceAreaTexture = new Texture(Gdx.files.internal("area_passage_to_entrance.png"));
		westPassageAreaTexture = new Texture(Gdx.files.internal("area_west_passage.png"));
		westHallwayAreaTexture = new Texture(Gdx.files.internal("area_west_hallway.png"));
		southPassageAreaTexture = new Texture(Gdx.files.internal("area_south_passage.png"));
		southHallwayAreaTexture = new Texture(Gdx.files.internal("area_south_hallway.png"));
		testRoomAreaTexture = new Texture(Gdx.files.internal("area_test_room.png"));
		developmentRoomAreaTexture = new Texture(Gdx.files.internal("area_development_room.png"));
		directorOfficeAreaTexture = new Texture(Gdx.files.internal("area_director_office.png"));
		serverRoomAreaTexture = new Texture(Gdx.files.internal("area_server_room.png"));
		officeAreaTexture = new Texture(Gdx.files.internal("area_office.png"));
		pathToExitAreaTexture = new Texture(Gdx.files.internal("area_passage_to_exit.png"));
		//box texture
		boxClosedTexture = new Texture(Gdx.files.internal("box.png"));
		boxOpenedTexture = new Texture(Gdx.files.internal("box_opened.png"));
		//card key texture
		cardKeyTexture = new Texture(Gdx.files.internal("card_key.png"));
		//projectile texture
		projectileTexture = new Texture(Gdx.files.internal("projectile.png"));

		//sprite sheets
		robotSpriteSheet = new Texture(Gdx.files.internal("robot_spritesheet.png"));
		player1SpriteSheet = new Texture(Gdx.files.internal("survivor1_spritesheet.png"));
		player2SpriteSheet = new Texture(Gdx.files.internal("survivor2_spritesheet.png"));
		player3SpriteSheet = new Texture(Gdx.files.internal("survivor3_spritesheet.png"));
		player4SpriteSheet = new Texture(Gdx.files.internal("survivor4_spritesheet.png"));
		gateObjectSpriteSheet = new Texture(Gdx.files.internal("gate_spritesheet.png"));
		doorObjectSpriteSheet = new Texture(Gdx.files.internal("door_spritesheet.png"));
		doorObjectRotatedSpriteSheet = new Texture(Gdx.files.internal("door_rotated_spritesheet.png"));
		//gateObjectRotatedSpriteSheet = new Texture(Gdx.files.internal("door_sample_rotated_spritesheet.png"));
	
		//robot animations
		TextureRegion[][] tmp = TextureRegion.split(robotSpriteSheet, robotSpriteSheet.getWidth() / FRAME_COLS_ROBOT, robotSpriteSheet.getHeight() / FRAME_ROWS_ROBOT);
		int k = 0;
		TextureRegion[] frames;
		//robot_standing - walk_north_west
		for (; k < 9; k++) {
			frames = new TextureRegion[4];
			for (int j = 0; j < 4; j++) {
				frames[j] = tmp[k][j];
			}
			robotAnimations[k] = new Animation<TextureRegion>(0.5f, frames);
		}
		//robot attack - robot attack_grabbing
		for (; k < 11; k++) {
			frames = new TextureRegion[6];
			for (int j = 0; j < 6; j++) {
				frames[j] = tmp[k][j];
			}
			robotAnimations[k] = new Animation<TextureRegion>(0.1f, frames);
		}
		//robot grab
		frames = new TextureRegion[4];
		for (int j = 0; j < 4; j++) {
			frames[j] = tmp[k][j];
		}
		robotAnimations[k] = new Animation<TextureRegion>(0.2f, frames);
		k++;
		//robot rush attack
		frames = new TextureRegion[FRAME_COLS_ROBOT];
		for (int j = 0; j < FRAME_COLS_ROBOT; j++) {
			frames[j] = tmp[k][j];
		}
		robotAnimations[k] = new Animation<TextureRegion>(0.2f, frames);

		//player1 animations
		tmp = TextureRegion.split(player1SpriteSheet, player1SpriteSheet.getWidth() / FRAME_COLS_PLAYER, player1SpriteSheet.getHeight() / FRAME_ROWS_PLAYER);
		for (int i = 0; i < FRAME_ROWS_PLAYER; i++) {
			frames = new TextureRegion[FRAME_COLS_PLAYER];
			for (int j = 0; j < FRAME_COLS_PLAYER; j++) {
				frames[j] = tmp[i][j];
			}
			player1Animations[i] = new Animation<TextureRegion>(0.25f, frames);
		}
		//player2 animations
		tmp = TextureRegion.split(player2SpriteSheet, player2SpriteSheet.getWidth() / FRAME_COLS_PLAYER, player2SpriteSheet.getHeight() / FRAME_ROWS_PLAYER);
		for (int i = 0; i < FRAME_ROWS_PLAYER; i++) {
			frames = new TextureRegion[FRAME_COLS_PLAYER];
			for (int j = 0; j < FRAME_COLS_PLAYER; j++) {
				frames[j] = tmp[i][j];
			}
			player2Animations[i] = new Animation<TextureRegion>(0.25f, frames);
		}
		//player3 animations
		tmp = TextureRegion.split(player3SpriteSheet, player3SpriteSheet.getWidth() / FRAME_COLS_PLAYER, player3SpriteSheet.getHeight() / FRAME_ROWS_PLAYER);
		for (int i = 0; i < FRAME_ROWS_PLAYER; i++) {
			frames = new TextureRegion[FRAME_COLS_PLAYER];
			for (int j = 0; j < FRAME_COLS_PLAYER; j++) {
				frames[j] = tmp[i][j];
			}
			player3Animations[i] = new Animation<TextureRegion>(0.25f, frames);
		}
		//player4 animations
		tmp = TextureRegion.split(player4SpriteSheet, player4SpriteSheet.getWidth() / FRAME_COLS_PLAYER, player4SpriteSheet.getHeight() / FRAME_ROWS_PLAYER);
		for (int i = 0; i < FRAME_ROWS_PLAYER; i++) {
			frames = new TextureRegion[FRAME_COLS_PLAYER];
			for (int j = 0; j < FRAME_COLS_PLAYER; j++) {
				frames[j] = tmp[i][j];
			}
			player4Animations[i] = new Animation<TextureRegion>(0.25f, frames);
		}

		//gate object animation
		tmp = TextureRegion.split(gateObjectSpriteSheet, gateObjectSpriteSheet.getWidth() / FRAME_COLS_OBJECT, gateObjectSpriteSheet.getHeight() / 1);
		frames = new TextureRegion[FRAME_COLS_OBJECT];
		for (int i = 0; i < FRAME_COLS_OBJECT; i++) {
			frames[i] = tmp[0][i];
		}
		gateAnimation = new Animation<TextureRegion>(GateObject.GATEOBJECT_FRAME_DURATION, frames); //3 frames in 0.5sec

		//door object animation
		tmp = TextureRegion.split(doorObjectSpriteSheet, doorObjectSpriteSheet.getWidth() / FRAME_COLS_OBJECT, doorObjectSpriteSheet.getHeight() / 1);
		frames = new TextureRegion[FRAME_COLS_OBJECT];
		for (int i = 0; i < FRAME_COLS_OBJECT; i++) {
			frames[i] = tmp[0][i];
		}
		doorAnimation = new Animation<TextureRegion>(DoorObject.DOOROBJECT_FRAME_DURATION, frames); //3 frames in 0.5sec
		
		//rotated door object animation
		tmp = TextureRegion.split(doorObjectRotatedSpriteSheet, doorObjectRotatedSpriteSheet.getWidth() / FRAME_COLS_OBJECT, doorObjectRotatedSpriteSheet.getHeight() / 1);
		frames = new TextureRegion[FRAME_COLS_OBJECT];
		for (int i = 0; i < FRAME_COLS_OBJECT; i++) {
			frames[i] = tmp[0][i];
		}
		doorAnimationRotated = new Animation<TextureRegion>(DoorObject.DOOROBJECT_FRAME_DURATION, frames); //3 frames in 0.5sec

		//remainTimeBoxClosedTexture = new Texture(Gdx.files.internal("remain_time_box_texture.png"));
		//Gdx.app.log("Assets", "" + (robotAnimations[16] == null));
		interactableMark = new Texture(Gdx.files.internal("interactable_mark.png"));
		portraitNormalTexture = new Texture(Gdx.files.internal("portrait_normal.png"));
		portraitKeyTexture = new Texture(Gdx.files.internal("portrait_cardkey.png"));
		portraitKilledTexture = new Texture(Gdx.files.internal("portrait_killed.png"));
		portraitDisconnectedTexture = new Texture(Gdx.files.internal("portrait_disconnected.png"));
		portraitEscapedTexture = new Texture(Gdx.files.internal("portrait_escaped.png"));
		portraitsTexture = new Texture(Gdx.files.internal("survivor_portraits.png"));
		portraitNormalDrawable = new TextureRegionDrawable(portraitNormalTexture);
		portraitKeyDrawable = new TextureRegionDrawable(portraitKeyTexture);
		portraitKilledDrawable = new TextureRegionDrawable(portraitKilledTexture);
		portraitDisconnectedDrawable = new TextureRegionDrawable(portraitDisconnectedTexture);
		portraitEscapedDrawable = new TextureRegionDrawable(portraitEscapedTexture);
		//portraitNormalTexture = new Texture(Gdx.files.internal("portrait_normal.png"));
		//portraitKeyTexture = new Texture(Gdx.files.internal("portrait_cardkey.png"));
		skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

		resultWindowTexture = new Texture(Gdx.files.internal("result_window.png"));

		introMatchInfoTexture = new Texture(Gdx.files.internal("intro_match_info.png"));
		introBackgroundTexture = new Texture(Gdx.files.internal("intro_development_room.png"));
		introRobotSpriteSheet = new Texture(Gdx.files.internal("intro_robot_spritesheet.png"));
		tmp = TextureRegion.split(introRobotSpriteSheet, introRobotSpriteSheet.getWidth() / 26, introRobotSpriteSheet.getHeight() / 1);
		frames = new TextureRegion[26];
		for (int i = 0; i < 26; i++) {
			frames[i] = tmp[0][i];
		}
		introRobotAnimation = new Animation<TextureRegion>(0.5f, frames);

		introRobotMalfunction = Gdx.audio.newSound(Gdx.files.internal("sound/intro_computer_break.wav"));
		introRobotVoice = Gdx.audio.newSound(Gdx.files.internal("sound/intro_robot_voice.wav"));
		introAlarm = Gdx.audio.newSound(Gdx.files.internal("sound/intro_alarm.wav"));
		introBackground = Gdx.audio.newSound(Gdx.files.internal("sound/intro_soundscape_computer.wav"));

		mainMenuBackgroundTexture = new Texture(Gdx.files.internal("main_menu_background.png"));

		//humanSounds = new Sound[1];
		//humanSounds[0] = Gdx.audio.newSound(Gdx.files.internal("sound/human_running.wav"));
		mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/458368__infinita08__sci-fi-theme.mp3"));
		mainMenuMusic.setLooping(true);
		mainMenuMusic.setVolume(0.3f);

		click = Gdx.audio.newSound(Gdx.files.internal("sound/click.wav"));

		resultWindowDown = Gdx.audio.newSound(Gdx.files.internal("sound/result_shutdown.wav"));
		resultPop = Gdx.audio.newSound(Gdx.files.internal("sound/result_pop.wav"));
		resultMessage = Gdx.audio.newSound(Gdx.files.internal("sound/result_alert.wav"));
	}

	public Sound[] getHumanSounds() {
		Sound[] humanSounds = new Sound[6];
		humanSounds[0] = Gdx.audio.newSound(Gdx.files.internal("sound/human_running.wav"));
		// humanSounds[1] = Gdx.audio.newSound(Gdx.files.internal("sound/human_dodge.wav"));
		humanSounds[1] = Gdx.audio.newSound(Gdx.files.internal("sound/394426__inspectorj__bamboo-swing-b6.wav"));
		humanSounds[2] = Gdx.audio.newSound(Gdx.files.internal("sound/snap.wav"));
		humanSounds[3] = Gdx.audio.newSound(Gdx.files.internal("sound/human_deathscream.wav"));
		humanSounds[4] = Gdx.audio.newSound(Gdx.files.internal("sound/slash.wav"));
		humanSounds[5] = Gdx.audio.newSound(Gdx.files.internal("sound/leafrustle.wav"));

		return humanSounds;
	}

	public Sound[] getRobotSounds() {
		Sound[] robotSounds = new Sound[5];
		robotSounds[0] = Gdx.audio.newSound(Gdx.files.internal("sound/robot_move.wav"));
		robotSounds[1] = Gdx.audio.newSound(Gdx.files.internal("sound/blade_slash.wav"));
		robotSounds[2] = Gdx.audio.newSound(Gdx.files.internal("sound/machine_transform.wav"));
		robotSounds[3] = Gdx.audio.newSound(Gdx.files.internal("sound/machine_noise.wav"));
		robotSounds[4] = Gdx.audio.newSound(Gdx.files.internal("sound/launch.wav"));

		return robotSounds;
	}

	@Override
	public void dispose() {
		mainAreaTexture.dispose();
		pathToEntranceAreaTexture.dispose();
		westPassageAreaTexture.dispose();
		westHallwayAreaTexture.dispose();
		southPassageAreaTexture.dispose();
		southHallwayAreaTexture.dispose();
		testRoomAreaTexture.dispose();
		developmentRoomAreaTexture.dispose();
		directorOfficeAreaTexture.dispose();
		serverRoomAreaTexture.dispose();
		officeAreaTexture.dispose();
		pathToExitAreaTexture.dispose();

		boxClosedTexture.dispose();
		cardKeyTexture.dispose();

		robotSpriteSheet.dispose();
		player1SpriteSheet.dispose();
		player2SpriteSheet.dispose();
		player3SpriteSheet.dispose();
		player4SpriteSheet.dispose();
		gateObjectSpriteSheet.dispose();
		doorObjectSpriteSheet.dispose();
		doorObjectRotatedSpriteSheet.dispose();

		interactableMark.dispose();
		portraitNormalTexture.dispose();
		portraitKeyTexture.dispose();
		portraitKilledTexture.dispose();

		skin.dispose();

		resultWindowTexture.dispose();

		introMatchInfoTexture.dispose();
		introBackgroundTexture.dispose();
		introRobotSpriteSheet.dispose();

		introRobotMalfunction.dispose();
		introRobotVoice.dispose();
		introAlarm.dispose();
		introBackground.dispose();

		mainMenuBackgroundTexture.dispose();

		mainMenuMusic.dispose();
		click.dispose();

		resultWindowDown.dispose();
		resultPop.dispose();
		resultMessage.dispose();
		//gateObjectRotatedSpriteSheet.dispose();
		Gdx.app.log("Assets", "disposed");
	}
}
