#include "jni.h"
#include "api.h"

jobject readTrack(sp_track *track, bool complete);
jobject readTrack(jobject target, sp_track *track, bool complete);
jobject readAlbum(sp_album *album, bool complete);
jobject readArtist(sp_artist *artist, bool complete);
void readArtist(jobject target, sp_artist *artist, bool complete);
void readArtistImage(sp_artist *artist, jobject target);
void readAlbumImage(sp_album *album, jobject target);

void linkToJstring(char *url, sp_link* l);
void imageToByteArray(const byte* coverId, jobject target);
void cb_image_loaded(sp_image *image, void *userdata);