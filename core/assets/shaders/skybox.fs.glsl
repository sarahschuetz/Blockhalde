#ifdef GL_ES
precision mediump float;
#endif

varying vec3 v_position;
uniform samplerCube u_cubeTexture;

void main() {
  gl_FragColor = textureCube(u_cubeTexture, v_position);
}
