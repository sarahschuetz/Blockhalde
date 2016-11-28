
attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

varying vec3 v_position;
varying vec3 v_normal;
varying vec2 v_texCoords;

uniform mat4 u_view;
uniform mat4 u_projection;
uniform mat4 u_normalMatrix;

void main() {
    mat4 vp = u_projection * u_view;

    v_position = (vp * vec4(a_position, 1.0)).xyz;
    v_normal = (u_normalMatrix * vec4(a_normal, 0.0)).xyz;
    v_texCoords = a_texCoord0;

    gl_Position = vp * vec4(a_position, 1.0);
}
