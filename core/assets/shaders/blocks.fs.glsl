#ifdef GL_ES
precision mediump float;
#endif

varying vec3 v_position;
varying vec3 v_normal;
varying vec2 v_texCoords;
varying vec3 v_eyeVec;
varying vec4 v_color;

uniform sampler2D u_texture;

void main() {
    gl_FragColor.rgb = texture2D(u_texture, v_texCoords).rgb * v_color.rrr;  //vec4(v_texCoords.x, v_texCoords.y, 0.0, 1.0)
	gl_FragColor.a = 1.0;
}
