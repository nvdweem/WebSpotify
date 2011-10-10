#include "media.h"
#include "Session.h"

void linkToJstring(char *url, sp_link* l) {
	sp_link_as_string(l, url, 256);
}

jobject readTrack(sp_track *track, bool complete) {
	char url[256];
	sp_link* link = sp_link_create_from_track(track, 0);
	linkToJstring(url, link);
	sp_link_release(link);
	jobject target = callObjectMethod(getSessionListener(), "createTrack", url);
	readTrack(target, track, complete);
	return target;
}

void setImage(jobject target, const byte* imageId) {
	if (!imageId)
		return;

	char url[256];
	sp_image *image = sp_image_create(getSession(), imageId);
	sp_link* link = sp_link_create_from_image(image);
	linkToJstring(url, link);
	callVoidMethod(target, "setImage", url);
	sp_link_release(link);
}

jobject readTrack(jobject target, sp_track *track, bool complete) {
	if (SP_ERROR_OTHER_PERMANENT == sp_track_error(track)) return NULL;
	if (callBoolMethod(target, "isComplete")) return target;

	callVoidMethod(target, "setName", sp_track_name(track));
	callVoidMethod(target, "setDuration", sp_track_duration(track));
	callVoidMethod(target, "setPopularity", sp_track_popularity(track));
	callVoidMethod(target, "setDisc", sp_track_disc(track));
	callVoidMethod(target, "setIndex", sp_track_index(track));
	callVoidMethod(target, "setAlbum", readAlbum(sp_track_album(track), false));
	callVoidMethod(target, "setArtist", readArtist(sp_track_artist(track, 0), false));
	setImage(target, sp_album_cover(sp_track_album(track)));

	callVoidMethod(target, "setComplete");
	return target;
}

void cb_albumbrowse_complete(sp_albumbrowse *browse, void* _target) {
	jobject target = (jobject) _target;
	callVoidMethod(target, "setReview", sp_albumbrowse_review(browse));
	for (int i = 0; i < sp_albumbrowse_num_tracks(browse); i++)
		callVoidMethod(target, "addTrack", readTrack(sp_albumbrowse_track(browse, i), true));

	callVoidMethod(target, "setComplete");
	callVoidMethod(target, "setBusy", false);
	removeGlobalRef(target);
	sp_albumbrowse_release(browse);
}

jobject readAlbum(sp_album *album, bool complete) {
	char url[256];
	sp_link* link = sp_link_create_from_album(album);
	linkToJstring(url, link);
	sp_link_release(link);

	jobject target = callObjectMethod(getSessionListener(), "createAlbum", url);

	if (callBoolMethod(target, "isComplete")) return target;

	// TODO: Figure out how Spotify somehow knows which replacement album to load
	// See Jonny Lang's bio, Lie to Me link.
	if (!sp_album_is_available(album)) {
		callVoidMethod(target, "setNotAvailable", true);
		callVoidMethod(target, "setComplete");
	}

	int type = -1;
	switch (sp_album_type(album)) {
		case SP_ALBUMTYPE_ALBUM: type = 0; break;
		case SP_ALBUMTYPE_SINGLE: type = 1; break;
		case SP_ALBUMTYPE_COMPILATION: type = 2; break;
		case SP_ALBUMTYPE_UNKNOWN: type = 3; break;
	}
	callVoidMethod(target, "setName", sp_album_name(album));
	callVoidMethod(target, "setType", type);
	callVoidMethod(target, "setYear", sp_album_year(album));
	callVoidMethod(target, "setArtist", readArtist(sp_album_artist(album), false));
	setImage(target, sp_album_cover(album));

	if (complete) {
		callVoidMethod(target, "setBusy", true);
		sp_albumbrowse_create(getSession(), album, &cb_albumbrowse_complete, getEnv()->NewGlobalRef(target));
	}
	else
		callVoidMethod(target, "setComplete");

	return target;
}

jobject readArtist(sp_artist *artist, bool complete) {
	if (!artist) return NULL;
	char url[256];
	sp_link* link = sp_link_create_from_artist(artist);
	linkToJstring(url, link);
	sp_link_release(link);
	jobject target = callObjectMethod(getSessionListener(), "createArtist", url);
	readArtist(target, artist, complete);
	return target;
}

void cb_artistbrowse_complete(sp_artistbrowse *browse, void *_target) {
	jobject target = (jobject) _target;
	callVoidMethod(target, "setBio", sp_artistbrowse_biography(browse));
	
	for (int i = 0; i < sp_artistbrowse_num_similar_artists(browse); i++) 
		callVoidMethod(target, "addRelatedArtist", readArtist(sp_artistbrowse_similar_artist(browse, i), false));
	
	for (int i = 0; i < sp_artistbrowse_num_tracks(browse); i++) {
		if (!sp_track_is_loaded(sp_artistbrowse_track(browse, i))) continue;
		callVoidMethod(target, "addTopTrack", readTrack(sp_artistbrowse_track(browse, i), true));
	}

	for (int i = 0; i < sp_artistbrowse_num_albums(browse); i++) {
		if (!sp_album_is_available(sp_artistbrowse_album(browse, i))) continue;
		callVoidMethod(target, "addAlbum", readAlbum(sp_artistbrowse_album(browse, i), true));
	}

	if (sp_artistbrowse_num_portraits(browse) > 0)
		setImage(target, sp_artistbrowse_portrait(browse, 0));

	callVoidMethod(target, "setComplete");
	callVoidMethod(target, "setBusy", false);
	sp_artistbrowse_release(browse);
	removeGlobalRef(target);
}
void readArtist(jobject target, sp_artist *artist, bool complete) {
	if (callBoolMethod(target, "isComplete")) return;
	callVoidMethod(target, "setName", sp_artist_name(artist));
	
	if (complete) {
		callVoidMethod(target, "setBusy", true);
		sp_artistbrowse_create(getSession(), artist, &cb_artistbrowse_complete, getEnv()->NewGlobalRef(target));
	}
	else
		callVoidMethod(target, "setComplete");
}

void cb_image_loaded(sp_image *image, void *userdata) {
	jobject target = (jobject) userdata;
	JNIEnv *env = attachThread();

	if (!image) {
		callVoidMethod(target, "setComplete");
		sp_image_release(image);
		env->DeleteGlobalRef(target);
		detachThread();
		return;
	}

	size_t size;
	const void* pData = sp_image_data(image, &size);
	jbyteArray byteArray = env->NewByteArray( size );

	jboolean isCopy = false;
	jbyte* pByteData = env->GetByteArrayElements( byteArray, &isCopy );

	for (size_t i = 0; i < size; i++)
		pByteData[i] = ( (byte*) pData)[i];
	env->ReleaseByteArrayElements(byteArray, pByteData, 0);

	sp_image_release(image);

	callVoidMethodB(env, target, "setImage", byteArray);
	callVoidMethod(target, "setComplete");
	env->DeleteGlobalRef(target);
	detachThread();
}
void imageToByteArray(const byte* coverId, jobject target) {
	if (!coverId) {
		callVoidMethod(target, "setComplete");
		removeGlobalRef(target);
		return;
	}

	sp_image *image = sp_image_create(getSession(), coverId);
	sp_image_add_load_callback(image, &cb_image_loaded, target);
}
void cb_artistbrowse_image_complete(sp_artistbrowse *result, void *userdata) {
	jobject target = (jobject) userdata;

	if (sp_artistbrowse_num_portraits(result) == 0) {
		callVoidMethod(target, "setComplete");
		sp_artistbrowse_release(result);
		return;
	}

	const byte* album = sp_artistbrowse_portrait(result, 0);
	imageToByteArray(album, target);
	sp_artistbrowse_release(result);
}
void readArtistImage(sp_artist *artist, jobject target) {
	if (callBoolMethod(target, "isComplete")) return;
	sp_artistbrowse_create(getSession(), artist, &cb_artistbrowse_image_complete, target);
}

void readAlbumImage(sp_album *album, jobject target) {
	if (callBoolMethod(target, "isComplete")) return;
	const byte* albumCover = sp_album_cover(album);
	imageToByteArray(albumCover, target);
}