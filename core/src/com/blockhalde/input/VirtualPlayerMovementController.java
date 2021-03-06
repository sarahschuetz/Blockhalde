package com.blockhalde.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.blockhalde.player.CameraComponent;
import com.blockhalde.player.DebugComponent;
import com.blockhalde.player.Player;
import com.blockhalde.player.PlayerDataComponent;
import com.blockhalde.player.PositionComponent;
import com.terrain.block.BlockType;
import com.terrain.world.WorldManagementSystem;
import com.util.PauseListener;

/**
 * An implementation of the {@link VirtualController} interface that controls the player movement.
 * @author shaendro
 */
public class VirtualPlayerMovementController extends VirtualAbstractController {
	private static final float GRAVITY = 9.8f;
	private static final float MAX_FALL_SPEED = GRAVITY * 5f;
	private static final float JUMP_STRENGTH = 13f;
	private static final float WALK_SPEED = 2f;
	private static final float FLY_SPEED = WALK_SPEED * 3f;
	private static final float PLAYER_HEIGHT = 1.6f;
	private static final float COLLISION_DISTANCE = 0.1f;
	private static final float MAX_MOVEMENT_PER_FRAME = 0.8f;

	private Keybindings keybindings = new Keybindings("util/keybindings.properties");

	private float movementFwrd = 0;
	private float movementSide = 0;
	private float movementUp = 0;
	private Vector3 movementVectorFwrd = new Vector3();
	private Vector3 movementVectorSide = new Vector3();
	private Vector3 movementVectorUp = new Vector3();
	private boolean canJump = false;
	private boolean jumping = false;

	/**
	 * Creates a {@link VirtualPlayerMovementController}.
	 * @param inputSystem The {@link InputSystem} the controller belongs to
	 */
	public VirtualPlayerMovementController(InputSystem inputSystem) {
		super(inputSystem);
		PauseListener.init();
	}

	@Override
	public void keyDown(int keycode) {
		if (active) {
			if (keycode == keybindings.getKey("FORWARD"))       movementFwrd += FLY_SPEED;
			else if (keycode == keybindings.getKey("LEFT"))     movementSide += FLY_SPEED;
			else if (keycode == keybindings.getKey("BACKWARD")) movementFwrd -= FLY_SPEED;
			else if (keycode == keybindings.getKey("RIGHT"))    movementSide -= FLY_SPEED;
			else if (keycode == keybindings.getKey("JUMP"))     jumping = true;
		}
	}

	@Override
	public void keyUp(int keycode) {
		if (active) {
			if (keycode == keybindings.getKey("FORWARD"))       movementFwrd -= FLY_SPEED;
			else if (keycode == keybindings.getKey("LEFT"))     movementSide -= FLY_SPEED;
			else if (keycode == keybindings.getKey("BACKWARD")) movementFwrd += FLY_SPEED;
			else if (keycode == keybindings.getKey("RIGHT"))    movementSide += FLY_SPEED;
			else if (keycode == keybindings.getKey("JUMP"))     jumping = false;
		}
	}

	@Override
	public void update(float deltaTime) {
		if (!PauseListener.isPaused() && active) {
			ImmutableArray<Entity> query = inputSystem.getEngine().getEntitiesFor(Family.all(PlayerDataComponent.class, CameraComponent.class).get());
			if (query.size() != 0) {
				Entity player = query.first();
				Vector3 oldPosition = player.getComponent(PositionComponent.class).getPosition().cpy();
				applyMovementFwrd(player, deltaTime);
				applyMovementSide(player, deltaTime);
				applyMovementUp(player, deltaTime);
				correctPosition(player, oldPosition);
				
				Camera camera = player.getComponent(CameraComponent.class).getCamera();
				camera.position.set(player.getComponent(PositionComponent.class).getPosition());
				camera.position.y += PLAYER_HEIGHT;
				camera.update();
			}
		}
	}

	/**
	 * Applies the {@link Player}s forward movement to his {@link PositionComponent}.
	 * @param player The {@link Player} {@link Entity}
	 * @param deltaTime The basic update deltaTime
	 */
	private void applyMovementFwrd(Entity player, float deltaTime) {
		Vector3 position = player.getComponent(PositionComponent.class).getPosition();
		Camera camera = player.getComponent(CameraComponent.class).getCamera();
		if (player.getComponent(DebugComponent.class).isFlying()) {
			float balancedMovementFwrd = deltaTime * FLY_SPEED * movementFwrd / (movementSide != 0 ? 2 : 1);
			movementVectorFwrd.set(camera.direction.x * balancedMovementFwrd, camera.direction.y * balancedMovementFwrd, camera.direction.z * balancedMovementFwrd);
		} else {
			float balancedMovementFwrd = deltaTime * WALK_SPEED * movementFwrd / (movementSide != 0 ? 2 : 1);
			if (balancedMovementFwrd > MAX_MOVEMENT_PER_FRAME) balancedMovementFwrd = MAX_MOVEMENT_PER_FRAME;
			movementVectorFwrd.set(camera.direction).rotate(camera.up, 90f).rotate(Vector3.Y, -90f).set(movementVectorFwrd.x * balancedMovementFwrd, movementVectorFwrd.y * balancedMovementFwrd, movementVectorFwrd.z * balancedMovementFwrd);
		}
		position.add(movementVectorFwrd);
	}

	/**
	 * Applies the {@link Player}s side movement to his {@link PositionComponent}.
	 * @param player The {@link Player} {@link Entity}
	 * @param deltaTime The basic update deltaTime
	 */
	private void applyMovementSide(Entity player, float deltaTime) {
		Vector3 position = player.getComponent(PositionComponent.class).getPosition();
		Camera camera = player.getComponent(CameraComponent.class).getCamera();
		if (player.getComponent(DebugComponent.class).isFlying()){
			float balancedMovementSide = deltaTime * FLY_SPEED * movementSide / (movementFwrd != 0 ? 2 : 1);			
			movementVectorSide.set(camera.direction).rotate(camera.up, 90f).set(movementVectorSide.x * balancedMovementSide, movementVectorSide.y * balancedMovementSide, movementVectorSide.z * balancedMovementSide);
		} else {
			float balancedMovementSide = deltaTime * WALK_SPEED * movementSide / (movementFwrd != 0 ? 2 : 1);
			if (balancedMovementSide > MAX_MOVEMENT_PER_FRAME) balancedMovementSide = MAX_MOVEMENT_PER_FRAME;		
			movementVectorSide.set(camera.direction).rotate(camera.up, 90f).set(movementVectorSide.x * balancedMovementSide, movementVectorSide.y * balancedMovementSide, movementVectorSide.z * balancedMovementSide);
		}
		position.add(movementVectorSide);
	}
	
	/**
	 * Applies the {@link Player}s downward movement to his {@link PositionComponent} and causes gravity.
	 * @param player The {@link Player} {@link Entity}
	 * @param deltaTime The basic update deltaTime
	 */
	private void applyMovementUp(Entity player, float deltaTime) {
		Vector3 position = player.getComponent(PositionComponent.class).getPosition();
		if (!player.getComponent(DebugComponent.class).isFlying()){
			if (!canJump) {
				movementUp -= GRAVITY * deltaTime * 5f;
				if (movementUp < -MAX_FALL_SPEED) movementUp = -MAX_FALL_SPEED;
			} else if (jumping) {
				movementUp = JUMP_STRENGTH;
				canJump = false;
			}
			movementVectorUp.set(0, movementUp * deltaTime, 0);
		} else {
			movementUp = 0;
			movementVectorUp.set(0, 0, 0);
		}
		position.add(movementVectorUp);
	}

	/**
	 * Corrects the modified {@link PositionComponent} of the {@link Player} to enable collision with blocks.
	 * @param player The {@link Player} {@link Entity}
	 * @param oldPosition The {@link PositionComponent} the {@link Player} had before his movement
	 */
	private void correctPosition(Entity player, Vector3 oldPosition) {
		Vector3 position = player.getComponent(PositionComponent.class).getPosition();
		if (!player.getComponent(DebugComponent.class).isFlying()){
			float xMoved = position.x - oldPosition.x;
			float yMoved = position.y - oldPosition.y;
			float zMoved = position.z - oldPosition.z;
			
			//Y Collision
			canJump = false;
			if (yMoved > 0 && blockAt(oldPosition.x, position.y + COLLISION_DISTANCE + PLAYER_HEIGHT, oldPosition.z)) {
				movementUp = 0;
				position.y = (int) (position.y + COLLISION_DISTANCE + PLAYER_HEIGHT) - COLLISION_DISTANCE - PLAYER_HEIGHT;
			}
			else if (yMoved < 0 && blockAt(oldPosition.x, position.y - COLLISION_DISTANCE, oldPosition.z)) {
				movementUp = 0;
				canJump = true;
				position.y = (int) (position.y - COLLISION_DISTANCE) + COLLISION_DISTANCE + 1;
			}

			//XZ Collision
			for (int heightModifier = 0; heightModifier < PLAYER_HEIGHT; heightModifier++) {
				float newPositionX = oldPosition.x + xMoved + COLLISION_DISTANCE * Math.signum(xMoved);
				float newPositionZ = oldPosition.z + zMoved + COLLISION_DISTANCE * Math.signum(zMoved);
				float heightModifiedY = position.y + heightModifier;
				boolean blockX = xMoved != 0 && blockAt(newPositionX, heightModifiedY, oldPosition.z);
				boolean blockZ = zMoved != 0 && blockAt(oldPosition.x, heightModifiedY, newPositionZ);
				boolean blockXZ = !blockX && !blockZ && blockAt(newPositionX, heightModifiedY, newPositionZ);
				if (blockX || blockXZ) {
					if (xMoved > 0) position.x = (int) oldPosition.x + (oldPosition.x > 0 ? 1 - COLLISION_DISTANCE : -COLLISION_DISTANCE);
					else position.x = (int) oldPosition.x + (oldPosition.x > 0 ? COLLISION_DISTANCE : -1 + COLLISION_DISTANCE);
				}
				if (blockZ) {
					if (zMoved > 0) position.z = (int) oldPosition.z + (oldPosition.z > 0 ? 1 - COLLISION_DISTANCE : -COLLISION_DISTANCE);
					else position.z = (int) oldPosition.z + (oldPosition.z > 0 ? COLLISION_DISTANCE : -1 + COLLISION_DISTANCE);
				}
				while (blockAt(position.x, heightModifiedY, position.z)) {
					position.y++;
					heightModifiedY = position.y + heightModifier;
				}
			}
		}
	}

	/**
	 * Queries the {@link WorldManagementSystem} for the {@link BlockType} at the given coordinates.
	 * @param x The x coordinate of the block
	 * @param y The y coordinate of the block
	 * @param z The z coordinate of the block
	 * @return True if the block at the position is not of {@link BlockType}.AIR
	 */
	private boolean blockAt(float x, float y, float z) {
		return inputSystem.getEngine().getSystem(WorldManagementSystem.class).getBlockType((int) x, (int) y, (int) z) != BlockType.AIR.getBlockId();
	}
}
