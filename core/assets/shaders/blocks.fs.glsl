#ifdef GL_ES
precision mediump float;
#endif

const float fogGradientWorldLength = 20.0;
const float fogDensity = 1.0;

const float aoFactor = 0.5;

varying vec4 v_viewPosition;
varying vec3 v_normal;
varying vec2 v_texCoords;
varying vec3 v_eyeVec;
varying vec4 v_color;

uniform sampler2D u_texture;
uniform sampler2D u_fogGradient;

void main() {
    float dist = abs(v_viewPosition.z / fogGradientWorldLength);
    float fogFactor = 0.0;

    fogFactor = 1.0 /exp(dist * fogDensity);
    fogFactor = clamp( fogFactor, 0.0, 1.0 );

    vec4 fogColor = texture2D(u_fogGradient, vec2(fogFactor, 0.5));

    float ao = v_color.r * aoFactor;

    gl_FragColor.rgb = mix(vec3(0.0, 0.0, 0.0), texture2D(u_texture, v_texCoords).rgb, ao);  //vec4(v_texCoords.x, v_texCoords.y, 0.0, 1.0)
	gl_FragColor.a = 1.0;

    gl_FragColor = mix(fogColor, gl_FragColor, fogFactor);
}
