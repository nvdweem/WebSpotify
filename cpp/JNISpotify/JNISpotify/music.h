#include "api.h"
#include "jni.h"

void setPlayer(jobject player);
jobject getPlayer();
int music_delivery(sp_session *session, const sp_audioformat *format, const void *frames, int num_frames);
