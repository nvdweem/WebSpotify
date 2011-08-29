#include "media.h"
#include "session.h"

void linkToJstring(char *url, sp_link* l) {
	sp_link_as_string(l, url, 256);
}

jobject readTrack(sp_track *track, bool complete) {
	char url[256];
	linkToJstring(url, sp_link_create_from_track(track, 0));
	jobject target = callObjectMethod(getSessionListener(), "createTrack", url);
	callVoidMethod(target, "setName", sp_track_name(track));
	callVoidMethod(target, "setDuration", sp_track_duration(track));
	callVoidMethod(target, "setPopularity", sp_track_popularity(track));
	callVoidMethod(target, "setDisc", sp_track_disc(track));

	if (complete) {
		callVoidMethod(target, "setAlbum", readAlbum(sp_track_album(track), false));
		callVoidMethod(target, "setArtist", readArtist(sp_track_artist(track, 0), false));
	}
	callVoidMethod(target, "setComplete");

	return target;
}

jobject readAlbum(sp_album *album, bool complete) {
	char url[256];
	linkToJstring(url, sp_link_create_from_album(album));
	jobject target = callObjectMethod(getSessionListener(), "createAlbum", url);

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
	
	if (complete) {
		callVoidMethod(target, "setArtist", readArtist(sp_album_artist(album), false));
		// Todo: Albumbrowse opstarten.
		callVoidMethod(target, "setComplete");
	}
	else
		callVoidMethod(target, "setComplete");

	return target;
}

jobject readArtist(sp_artist *artist, bool complete) {
	char url[256];
	linkToJstring(url, sp_link_create_from_artist(artist));
	jobject target = callObjectMethod(getSessionListener(), "createArtist", url);

	callVoidMethod(target, "setName", sp_artist_name(artist));

	if (complete) {
		// Todo: ArtistBrowse opstarten.
		callVoidMethod(target, "setComplete");
	}
	else
		callVoidMethod(target, "setComplete");

	return target;
}

void cb_image_loaded(sp_image *image, void *userdata) {
//fprintf(stderr, "a");
	jobject target = (jobject) userdata;
	JNIEnv *env = attachThread();

	if (!image) {
		callVoidMethod(target, "setComplete");
		env->DeleteGlobalRef(target);
		detachThread();
		return;
	}

//fprintf(stderr, "b");
	size_t size;
	const void* pData = sp_image_data(image, &size);
	jbyteArray byteArray = env->NewByteArray( size );
//fprintf(stderr, "v");
	jboolean isCopy = false;
	jbyte* pByteData = env->GetByteArrayElements( byteArray, &isCopy );
//fprintf(stderr, "c");
	for (size_t i = 0; i < size; i++)
		pByteData[i] = ( (byte*) pData)[i];
	env->ReleaseByteArrayElements(byteArray, pByteData, 0);
//fprintf(stderr, "d");
	sp_image_release(image);
//fprintf(stderr, "e");
	
	callVoidMethodB(env, target, "setImage", byteArray);
//fprintf(stderr, "g");
	callVoidMethod(target, "setComplete");
//fprintf(stderr, "h");
	env->DeleteGlobalRef(target);
//fprintf(stderr, "i");
	detachThread();
//fprintf(stderr, "J\n");
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
void cb_artistbrowse_complete(sp_artistbrowse *result, void *userdata) {
	jobject target = (jobject) userdata;

	if (sp_artistbrowse_num_portraits(result) == 0) {
		callVoidMethod(target, "setComplete");
		sp_artistbrowse_release(result);
		return;
	}

	const byte* album = sp_artistbrowse_portrait(result, 0);
	imageToByteArray(album, target);
}
void readArtistImage(sp_artist *artist, jobject target) {
	sp_artistbrowse_create(getSession(), artist, &cb_artistbrowse_complete, target);
}

void readAlbumImage(sp_album *album, jobject target) {
	const byte* albumCover = sp_album_cover(album);
	imageToByteArray(albumCover, target);
}