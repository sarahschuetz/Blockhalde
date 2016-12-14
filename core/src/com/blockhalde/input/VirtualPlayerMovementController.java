package com.blockhalde.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.blockhalde.player.CameraComponent;
import com.blockhalde.player.DebugComponent;
import com.blockhalde.player.PlayerDataComponent;
import com.blockhalde.player.PositionComponent;
import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.terrain.world.WorldManagementSystem;
import com.util.PauseListener;

/**
 * An implementation of the {@link VirtualController} interface that controls the player movement.
 * @author shaendro
 */
public class VirtualPlayerMovementController extends VirtualAbstractController {
	private static final float GRAVITY = 0.98f;
	private static final float MAX_FALL_SPEED = -4f;
	private static final float FLY_SPEED = 6f;
	private static final float WALK_SPEED = 2f;
	private static final float JUMP_STRENGTH = 0.3f;
	private static final float PLAYER_HEIGHT = 2f;
	private static final float COLLISION_DISTANCE = 0.01f;
	private static final float VERTICAL_PEEK_DISTANCE = 0.5f;

	private Keybindings keybindings = new Keybindings("util/keybindings.properties");

	private float movementFwrd = 0;
	private float movementSide = 0;
	private float movementDown = 0;
	private Vector3 movementVectorFwrd = new Vector3();
	private Vector3 movementVectorSide = new Vector3();
	private Vector3 movementVectorDown = new Vector3();
	private boolean jumping;

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
				Camera camera = player.getComponent(CameraComponent.class).getCamera();
				Vector3 position = player.getComponent(PositionComponent.class).getPosition();
				boolean flying = player.getComponent(DebugComponent.class).isFlying();
				Vector3 oldPosition = position.cpy();
				position.add(prepareMovementVectorFwrd(camera, flying, deltaTime));
				position.add(prepareMovementVectorSide(camera, flying, deltaTime));
				position.add(prepareMovementVectorDown(oldPosition, flying, deltaTime));
				correctPosition(oldPosition, position, flying);
				camera.position.set(position.x, position.y + PLAYER_HEIGHT, position.z);
				camera.update();
			}
		}
	}

	private Vector3 prepareMovementVectorFwrd(Camera camera, boolean flying, float deltaTime) {
		if (flying) {
			float balancedMovementFwrd = deltaTime * FLY_SPEED * movementFwrd / (movementSide != 0 ? 2 : 1);
			return movementVectorFwrd.set(camera.direction.x * balancedMovementFwrd, camera.direction.y * balancedMovementFwrd, camera.direction.z * balancedMovementFwrd);
		} else {
			float balancedMovementFwrd = deltaTime * WALK_SPEED * movementFwrd / (movementSide != 0 ? 2 : 1);
			return movementVectorFwrd.set(camera.direction).rotate(camera.up, 90f).rotate(Vector3.Y, -90f).set(movementVectorFwrd.x * balancedMovementFwrd, movementVectorFwrd.y * balancedMovementFwrd, movementVectorFwrd.z * balancedMovementFwrd);
		}
	}

	private Vector3 prepareMovementVectorSide(Camera camera, boolean flying, float deltaTime) {
		if (flying){
			float balancedMovementSide = deltaTime * FLY_SPEED * movementSide / (movementFwrd != 0 ? 2 : 1);			
			return movementVectorSide.set(camera.direction).rotate(camera.up, 90f).set(movementVectorSide.x * balancedMovementSide, movementVectorSide.y * balancedMovementSide, movementVectorSide.z * balancedMovementSide);
		} else {
			float balancedMovementSide = deltaTime * WALK_SPEED * movementSide / (movementFwrd != 0 ? 2 : 1);			
			return movementVectorSide.set(camera.direction).rotate(camera.up, 90f).set(movementVectorSide.x * balancedMovementSide, movementVectorSide.y * balancedMovementSide, movementVectorSide.z * balancedMovementSide);
		}
	}

	private Vector3 prepareMovementVectorDown(Vector3 position, boolean flying, float deltaTime) {
		if (!flying){
			if (!isBlockAt(position.x, position.y - VERTICAL_PEEK_DISTANCE, position.z)) {
				movementDown -= GRAVITY * deltaTime;
				if (movementDown < MAX_FALL_SPEED) movementDown = MAX_FALL_SPEED;
			} else {
				if (jumping) movementDown = JUMP_STRENGTH;
				else if (movementDown < 0) movementDown = 0;
			}
			return movementVectorDown.set(0, movementDown, 0);
		} else {
			movementDown = 0;
			return movementVectorDown.set(0, 0, 0);
		}
	}

	private void correctPosition(Vector3 oldPosition, Vector3 position, boolean flying) {
		if (!flying){
			if (movementDown == 0.0) position.y = (int) position.y + COLLISION_DISTANCE;

			int xMoved = (int) oldPosition.x - (int) position.x;
			int zMoved = (int) oldPosition.z - (int) position.z;
			boolean blocked = isBlockAt(position.x, position.y, position.z) || isBlockAt(position.x, position.y + 1, position.z);
			boolean blockedX = isBlockAt(oldPosition.x - xMoved,  position.y,  oldPosition.z) || isBlockAt(oldPosition.x - xMoved,  position.y + 1,  oldPosition.z);
			boolean blockedZ = isBlockAt(oldPosition.x,  position.y,  oldPosition.z - zMoved) || isBlockAt(oldPosition.x,  position.y + 1,  oldPosition.z - zMoved);

			if (blocked) {
				if (xMoved != 0 && (blockedX || zMoved != 0 && !blockedZ)) {
					if (xMoved > 0) position.x = (int) position.x + (oldPosition.x > 0 ? 1 + COLLISION_DISTANCE : COLLISION_DISTANCE);
					else position.x = (int) position.x + (oldPosition.x > 0 ? -COLLISION_DISTANCE : -1 - COLLISION_DISTANCE);
				}
				if (zMoved != 0 && blockedZ) {
					if (zMoved > 0) position.z = (int) position.z + (oldPosition.z > 0 ? 1 + COLLISION_DISTANCE : COLLISION_DISTANCE);
					else position.z = (int) position.z + (oldPosition.z > 0 ? -COLLISION_DISTANCE : -1 - COLLISION_DISTANCE);
				}
				if (xMoved == 0 && zMoved == 0) {
					position.y = (int) position.y + 1;
				}
			}
		}
	}

	private boolean isBlockAt(float x, float y, float z) {
		x = x < 0 ? x - Chunk.X_MAX : x;
		z = z < 0 ? z - Chunk.Z_MAX : z;
		return inputSystem.getEngine().getSystem(WorldManagementSystem.class).getBlockType((int) x, (int) y, (int) z) != BlockType.AIR.getBlockId();
	}
}
