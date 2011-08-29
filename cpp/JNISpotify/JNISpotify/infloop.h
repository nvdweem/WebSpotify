#include "api.h"
#include <windows.h>

void init_main_thread();
void notify_main_thread(sp_session *session);
void infloop(sp_session *session);