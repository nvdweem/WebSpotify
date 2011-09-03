TARGET=libJNISpotify.so
CC = g++
CFLAGs += -Werror
CFLAGs += -I/usr/lib/jvm/java-1.6.0-openjdk/include/ -I/usr/local/include/libspotify/ -fPIC
CXXFLAGS = $(CFLAGs)

include common.mk

$(TARGET): JNI.o  media.o  music.o  playlist.o  Session.o
	$(CC) $(CFLAGS) $(LDFLAGS) $(LDLIBS) -shared $^ -o $@
ifdef DEBUG
ifeq ($(shell uname),Darwin)
	install_name_tool -change @loader_path/../Frameworks/libspotify.framework/libspotify @rpath/libspotify.so $@
endif
endif
