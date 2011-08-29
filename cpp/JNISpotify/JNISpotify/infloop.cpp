
#include "infloop.h"
#include <jni.h>

static HANDLE events;

void init_main_thread() {
	events = CreateEvent(NULL, FALSE, FALSE, NULL);
}

void notify_main_thread(sp_session *session) {
	SetEvent(events);
}

void infloop(sp_session *session) {
	int next_timeout = 0;

	while (true) {
		WaitForSingleObject(events, next_timeout > 0 ? next_timeout : INFINITE);

		do {
			sp_session_process_events(session, &next_timeout);
		} while (next_timeout == 0);
	}
}