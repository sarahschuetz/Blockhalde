#ifdef GL_ES
precision mediump float;
#endif

varying vec3 v_position;
uniform samplerCube u_cubeTexture;

void main() {
  gl_FragColor = texture(cube_texture, v_position);
}
