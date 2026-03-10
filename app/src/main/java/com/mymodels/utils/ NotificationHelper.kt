object NotificationHelper {

    fun runningNotification(context: Context) =
        NotificationCompat.Builder(context, "ai")
            .setContentTitle("MyModels")
            .setContentText("AI sedang berpikir...")
            .setSmallIcon(R.drawable.ic_launcher)
            .build()

    fun answerNotification(context: Context, text: String) {

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notif = NotificationCompat.Builder(context, "ai")
            .setContentTitle("Jawaban siap")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher)
            .build()

        manager.notify(2, notif)
    }
}