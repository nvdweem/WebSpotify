#include "jni.h"
#include "api.h"

jobject readTrack(sp_track *track, bool complete);
jobject readTrack(jobject target, sp_track *track, bool complete);
jobject readAlbum(sp_album *album, bool complete);
jobject readArtist(sp_artist *artist, bool complete);
void readArtist(jobject target, sp_artist *artist, bool complete);
void readArtistImage(sp_artist *artist, jobject target);
void readAlbumImage(sp_album *album, jobject target);