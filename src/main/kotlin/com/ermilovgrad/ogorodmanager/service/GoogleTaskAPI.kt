package com.ermilovgrad.ogorodmanager.service

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.tasks.Tasks
import com.google.api.services.tasks.TasksScopes
import com.google.api.services.tasks.model.Task
import com.google.api.services.tasks.model.TaskList
import com.google.api.services.tasks.model.TaskLists
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.security.GeneralSecurityException

enum class EVENT_ACTIONS {ADD, REPLACE, DELETE, NONE}

/* class to demonstarte use of Calendar events list API */
@Component("googleTaskAPI")
class GoogleTaskAPI {

    private val DELIMETER = "_"

    /**
     * Application name.
     */
    private val APPLICATION_NAME = "ErmilovCalendar"

    /**
     * Global instance of the JSON factory.
     */
    private val JSON_FACTORY: GsonFactory = GsonFactory.getDefaultInstance()

    /**
     * Directory to store authorization tokens for this application.
     */
    private val TOKENS_DIRECTORY_PATH = "tokens_task"

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private val SCOPES = listOf<String>(TasksScopes.TASKS)

    private val CREDENTIALS_FILE_PATH = "/credentials.json"

    private val user = "sheburshesha@gmail.com"

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @Throws(IOException::class)
    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
        // Load client secrets.
        val `in` =
            GoogleTaskAPI::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
                ?: throw FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH)
        val clientSecrets: GoogleClientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(`in`))

        // Build flow and trigger user authorization request.
        val flow: GoogleAuthorizationCodeFlow = GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
        )
            .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build()
        val receiver: LocalServerReceiver = LocalServerReceiver.Builder().setPort(8888).build()
        //returns an authorized Credential object.
        return AuthorizationCodeInstalledApp(flow, receiver).authorize(user)
    }

    @Throws(Exception::class)
    fun getService(): Tasks {
        // Build a new authorized API client service.
        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
        return Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build()
    }

    @Throws(IOException::class, GeneralSecurityException::class)
    fun getTasks(service: Tasks, count: Int): List<TaskList> {
        // Print the count task lists.
        val result: TaskLists = service.tasklists().list()
            .setMaxResults(if(count == 0) 2500 else count)
            .execute()
        val taskLists: List<TaskList> = result.getItems()
        println("taskLists size:${taskLists.size}")
        if (taskLists.isNullOrEmpty()) {
            println("No task lists found.")
        } else {
            println("Task lists:")
            for (tasklist in taskLists) {
                println("tasklist:${tasklist}")
                val tasks = service.tasks().list(tasklist.id)
                    .setMaxResults(if(count == 0) 2500 else count)
                    .execute()
                println("tasks size:${tasks.values.size}")
                for (task in tasks.values) {
                    println("task:${task}")
                }
            }
        }
        return taskLists
    }

    @Throws(IOException::class, GeneralSecurityException::class)
    fun insertTask(service: Tasks, tasklistId: String, task: Task): Tasks.TasksOperations.Insert {
        return service.tasks().insert(tasklistId, task);
    }

//    @Throws(IOException::class, GeneralSecurityException::class)
//    fun updateTask(service: Tasks, count: Int): List<TaskList> {
//        service.TasksOperations().update().execute()
//    }



//    fun getForObject(): Tattoo? = try {
//        restTemplate.getForObject(
//            UriComponentsBuilder
//                .fromHttpUrl(tattooServiceConfiguration.url)
//                .path("/tattoo/123")
//                .build()
//                .toUri(),
//            Tattoo::class.java
//        )
//    } catch (e: RestClientResponseException) {
//        null
//    }

//    fun addOrReplaceTask(orderPlanCalc: OrderPlanCalc, tasks: List<TaskList>): Pair<EVENT_ACTIONS, TaskList> {
//        lateinit var type: EVENT_ACTIONS
//        val event: TaskList? = tasks.find { it.id.replaceAfterLast(DELIMETER, "").equals(orderPlanCalc.summary.replaceAfterLast(DELIMETER, "")) }
//        if (!event.isNullOrEmpty()) {
//            if (orderPlanCalc.statusId == 1.toByte()) {
//                type = EVENT_ACTIONS.DELETE
//            }
//            if (event.summary.equals(orderPlanCalc.summary)) {
//                type = EVENT_ACTIONS.NONE
//            } else {
//                event.setDescription(orderPlanCalc.formSummary(orderPlanCalc))
//                type = EVENT_ACTIONS.REPLACE
//            }
//        }
//        return Pair(type, event!!)
//    }
}
