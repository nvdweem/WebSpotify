#include "music.h"
#include "JNI.h"
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

jobject player;
void setPlayer(jobject _player) {
	player = _player;
}

jobject getPlayer() {
	return player;
}

/**
 * This callback is used from libspotify whenever there is PCM data available.
 *
 * @sa sp_session_callbacks#music_delivery
 */
int music_delivery(sp_session *sess, const sp_audioformat *format, const void *frames, int num_frames)
{
	if (num_frames == 0)
		return 0; // Audio discontinuity, do nothing

	callVoidMethod(player, "setAudioFormat", format->sample_rate, format->channels);

	JNIEnv *env = attachThread();

	int sampleSize = 2 * format->channels;
	int numBytes = num_frames * sampleSize;
	jbyteArray byteArray = env->NewByteArray( numBytes );
    for (int i=0; i<numBytes; i++)
		env->SetByteArrayRegion( byteArray, i, 1, &( (jbyte*) frames)[i] );
	int buffered = callIntMethod(env, player, "addToBuffer", byteArray);
	
	detachThread();

	return buffered;
}