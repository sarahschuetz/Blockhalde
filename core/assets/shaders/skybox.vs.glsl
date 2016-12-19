
attribute vec3 a_position;

varying vec3 v_position;

uniform mat4 u_view;
uniform mat4 u_projection;

void main() {
  v_position = a_position;
  gl_Position = u_projection * u_view * vec4(a_position, 1.0);
}
