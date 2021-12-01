package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

import net.MsgCodes;

/**
 * player character
 */
public class Player extends Actor implements Disposable {
	int playerNum;

	public boolean disconnected = false;

	private Animation<TextureRegion>[] animations;
	private Animation<TextureRegion> curAnimation;
	private TextureRegion curFrame;
	private TextureRegionDrawable portrait;

	private Sound[] sounds;
	private float soundDurationRunning = Assets.SOUND_INTERVAL_RUNNING;
	private float soundDurationDodge = Assets.SOUND_INTERVAL_DODGE;
	private float soundDurationAttack = Assets.SOUND_INTERVAL_ATTACK;
	private float soundDurationRush = Assets.SOUND_INTERVAL_RUSH;
	private float soundVolume;

	private Area curArea; //area where the player is located

	private char curState; //current state of this character
	private char curDirection; //the direction this character is heading at

	private float projectileX; //used by robot
	private float projectileY; //used by robot

	private Interactable nearbyObject;
	private boolean hasKey;
	private float stateTime; //to track elapsed time for the animation

	private boolean dragged; //used in playSound()
	private boolean grabbing; //used in playSound()
	private boolean interacted; //used in playSound()
	private boolean dead;

	Rectangle attackBound; //bounding rectangle of the robot bounding its attack range

	//constructor
	public Player(int playerNum, String name, Animation<TextureRegion>[] animations) {
		this.playerNum = playerNum;
		setName(name);
		this.animations = animations;

		setSize(Prototype.CHAR_WIDTH, Prototype.CHAR_HEIGHT);
		attackBound = new Rectangle(getX() - Prototype.ATTACK_RANGE, getY() - Prototype.ATTACK_RANGE, Prototype.CHAR_WIDTH + Prototype.ATTACK_RANGE*2, Prototype.CHAR_HEIGHT + Prototype.ATTACK_RANGE*2);

		stateTime = 0f;

		//configure action
		RepeatAction loop = new RepeatAction();
		loop.setCount(RepeatAction.FOREVER);
		loop.setAction(new Action() {
			@Override
			public boolean act(float delta) {
				curArea = curArea.determineArea(getX(), getY()); //determine player's area according to player's location.
				
				nearbyObject = curArea.checkCollision((Player)getActor()); //check if the player is adjacent to any object in the area

				//if player is currently interacting with nearbyObject
				if (nearbyObject != null) {
					if (curState == MsgCodes.Game.INTERACT_STATE) {
						nearbyObject.setInteracting(true);
					}
					else if (curState == MsgCodes.Game.INTERACT_SUCCESS_STATE) {
						nearbyObject.setInteracted(true, true);
						nearbyObject.setInteracting(false);
					}
					else if (curState == MsgCodes.Game.INTERACT_FAILED_STATE) {
						nearbyObject.setInteracted(true, false);
						nearbyObject.setInteracting(false);
					}
				}

				updateAttackBound(getX(), getY());
				updateAnimation(delta);

				playSound(delta);

				return true;
			}

		});

		addAction(loop);
	}

	public void setSounds(Sound[] sounds) {
		this.sounds = sounds;
		this.sounds[0].loop();
		this.sounds[0].pause();
	}

	public Sound[] getSounds() {
		return sounds;
	}

	public void setCurrentArea(Area curArea) {
		this.curArea = curArea;
	}

	public void updateAttackBound(float x, float y) {
		attackBound.setPosition(x - Prototype.ATTACK_RANGE, y - Prototype.ATTACK_RANGE);
	}

	public Area getCurrentArea() {
		return curArea;
	}

	public Interactable getNearbyObject() {
		return nearbyObject;
	}

	/**
	 * access to position value to get or set its position
	 * It is synchronized
	 * @param operation
	 * @param x
	 * @param y
	 * @return
	 */
	public synchronized float[] accessPosition(String operation, float x, float y) {
		if (operation.contentEquals("set")) {
			setX(x);
			setY(y);
		}
		else if (operation.contentEquals("get")) {
			return new float[] {getX(), getY()};
		}
		return null;
	}

	public synchronized char accessState(String operation, char state) {
		if (operation.contentEquals("set")) {
			if (curState != state && curState != MsgCodes.Game.ATTACK_GRABBING_STATE) {
				stateTime = 0;
				if (state == MsgCodes.Game.DRAGGED_STATE)
					dragged = true;
				if (state == MsgCodes.Game.GRABBING_STATE)
					grabbing = true;
				if (state == MsgCodes.Game.INTERACT_STATE)
					interacted = true;
			}
			curState = state;
		}
		else if (operation.contentEquals("get")) {
			return curState;
		}
		return 0;
	}

	public synchronized boolean accessHasKey(String operation, boolean hasKey) {
		if (operation.contentEquals("set")) {
			this.hasKey = hasKey;
		} else if (operation.contentEquals("get")) {
			return this.hasKey;
		}
		return false;
	}

	public synchronized char accessDirection(String operation, char direction) {
		if (operation.contentEquals("set")) {
			curDirection = direction;
		} else if (operation.contentEquals("get")) {
			return curDirection;
		}
		return 0;
	}

	public synchronized float[] accessProjectilePos(String operation, float x, float y) {
		if (operation.contentEquals("set")) {
			projectileX = x;
			projectileY = y;
		}
		else if (operation.contentEquals("get")) {
			return new float[] {projectileX, projectileY};
		}
		return null;
	}

	public void setPortraitDrawable(TextureRegionDrawable texture) {
		portrait = texture;
	}

	public TextureRegionDrawable getPortraitDrawable() {
		return portrait;
	}

	private void updateAnimation(float delta) {
		stateTime += delta;
		boolean loop = false;
		
		if (curState == MsgCodes.Game.NORMAL_STATE_MOVING) {
			int dir = Integer.parseInt(String.valueOf(curDirection));
			curAnimation = animations[dir];

			loop = true;
		}
		else if (curState == MsgCodes.Game.DODGE_STATE) {
			curAnimation = animations[Integer.parseInt(String.valueOf(curDirection)) + 8];
		}
		else if (curState == MsgCodes.Game.INTERACT_STATE) {
			if (playerNum == Prototype.PLAYER_ROBOT_NUM) {
				curAnimation = animations[Assets.ANIMATION_INTERACT_ROBOT];
			}
			else
				curAnimation = animations[Assets.ANIMATION_INTERACT];
			loop = true;
		}
		else if (curState == MsgCodes.Game.DRAGGED_STATE) {
			curAnimation = animations[Assets.ANIMATION_DRAGGED];
		}
		else if (curState == MsgCodes.Game.DEAD_STATE) {
			curAnimation = animations[Assets.ANIMATION_DIE];
		}
		else if (curState == MsgCodes.Game.ATTACK_STATE) {
			curAnimation = animations[Assets.ANIMATION_ATTACK];
		}
		else if (curState == MsgCodes.Game.ATTACK_GRABBING_STATE) {
			curAnimation = animations[Assets.ANIMATION_ATTACK_GRABBING];
		}
		else if (curState == MsgCodes.Game.GRABBING_STATE) {
			curAnimation = animations[Assets.ANIMATION_GRAB];
		}
		else if (curState == MsgCodes.Game.RUSH_STATE) {
			curAnimation = animations[Assets.ANIMATION_RUSH];
		}
		else {
			curAnimation = animations[Assets.ANIMATION_STILL];
			loop = true;
		}

		curFrame = curAnimation.getKeyFrame(stateTime, loop);
	}

	public void setSoundVolume(float volume) {
		soundVolume = volume;
	}

	private void playSound(float delta) {
		soundDurationRunning += delta;
		soundDurationDodge += delta;
		soundDurationAttack += delta;
		soundDurationRush += delta;
		if (sounds != null) {
			if (curState == MsgCodes.Game.NORMAL_STATE_MOVING) {
				if (soundDurationRunning >= Assets.SOUND_INTERVAL_RUNNING) {
					soundDurationRunning = 0;
					sounds[0].play(soundVolume);
				}
			}
			else if (curState == MsgCodes.Game.DODGE_STATE) {
				if (soundDurationDodge >= Assets.SOUND_INTERVAL_DODGE) {
					soundDurationDodge = 0;
					sounds[1].play(soundVolume);
				}
			}
			else if (curState == MsgCodes.Game.DRAGGED_STATE) {
				if (dragged) {
					dragged = false;
					sounds[2].play(soundVolume * 0.8f);
				}
			}
			else if (curState == MsgCodes.Game.DEAD_STATE) {
				if (!dead) {
					dead = true;
					sounds[3].play(soundVolume);
					sounds[4].play(soundVolume);
				}
			}
			else if (curState == MsgCodes.Game.INTERACT_STATE && playerNum != Prototype.PLAYER_ROBOT_NUM) {
				if (interacted) {
					interacted = false;
					sounds[5].play(soundVolume * 0.5f);
				}
			}
			else if (curState == MsgCodes.Game.ATTACK_STATE || curState == MsgCodes.Game.ATTACK_GRABBING_STATE) {
				if (soundDurationAttack >= Assets.SOUND_INTERVAL_ATTACK) {
					soundDurationAttack = 0;
					sounds[1].stop();
					sounds[2].stop();
					sounds[1].play(soundVolume * 0.3f);
					sounds[2].play(soundVolume);
				}
			}
			else if (curState == MsgCodes.Game.RUSH_STATE) {
				if (soundDurationRush >= Assets.SOUND_INTERVAL_RUSH && stateTime <= 2) {
					soundDurationRush = 0;
					sounds[1].stop();
					sounds[2].stop();
					sounds[3].stop();
					sounds[1].play(soundVolume * 0.4f);
					sounds[2].play(soundVolume);
					sounds[3].play(soundVolume * 0.8f);
				}
			}
			else if (curState == MsgCodes.Game.GRABBING_STATE) {
				if (grabbing) {
					grabbing = false;
					sounds[4].play(soundVolume);
				}
			}
			else {
				sounds[0].stop();
				sounds[1].stop();
				sounds[2].stop();
				sounds[3].stop();
				if (playerNum != Prototype.PLAYER_ROBOT_NUM)
					sounds[5].stop();
			}
		}
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		batch.draw(curFrame, getX()-Prototype.ATTACK_RANGE, getY()-Prototype.ATTACK_RANGE);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		if (sounds != null) {
			for (Sound sound : sounds) {
				sound.dispose();
			}
		}
		Gdx.app.log("Player", "disposed");
	}
}
