#ifdef LOGTRACE
#define LOG_TRACE(args...) printf(args)
#else
#define LOG_TRACE(args...)
#endif

#ifdef LOGPOLLTRACE
#define LOG_POLL_TRACE(args...) LOG_TRACE(args...)
#else
#define LOG_POLL_TRACE(args...)
#endif
