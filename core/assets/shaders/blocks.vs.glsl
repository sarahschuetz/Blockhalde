

attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
attribute vec4 a_color;

varying vec4 v_viewPosition;
varying vec3 v_normal;
varying vec2 v_texCoords;
varying vec3 v_eyeVec;
varying vec4 v_color;

uniform mat4 u_view;
uniform mat4 u_projection;
uniform mat4 u_normalMatrix;

void main() {
    mat4 vp = u_projection * u_view;

    v_viewPosition = (u_view * vec4(a_position, 1.0));
    v_normal = (u_normalMatrix * vec4(a_normal, 0.0)).xyz;
    v_texCoords = a_texCoord0;
    v_color = a_color;
    
	vec4 eyePosition = u_view * vec4(a_position,1);
    v_eyeVec = -eyePosition.xyz;
    
    gl_Position = vp * vec4(a_position, 1.0);
}
