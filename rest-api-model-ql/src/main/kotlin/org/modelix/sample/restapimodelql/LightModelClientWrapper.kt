package org.modelix.sample.restapimodelql

import University.Schedule.*
import io.ktor.server.routing.*
import io.ktor.utils.io.charsets.*
import jetbrains.mps.lang.core.N_BaseConcept
import kotlinx.coroutines.launch
import org.modelix.client.light.LightModelClient
import org.modelix.metamodel.*
import org.modelix.model.api.INodeReference
import org.modelix.model.api.INodeReferenceSerializer
import org.modelix.model.api.serialize
import org.modelix.model.repositoryconcepts.N_Module
import org.modelix.model.repositoryconcepts.N_Repository
import org.modelix.model.repositoryconcepts.models
import org.modelix.model.repositoryconcepts.rootNodes
import org.modelix.model.server.api.buildModelQuery
import org.modelix.sample.restapimodelql.models.Room
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URLDecoder

private val logger: Logger = LoggerFactory.getLogger("org.modelix.sample.restapimodelql.ModelServerLightWrapper")

class LightModelClientWrapper(
    private val host: String = "localhost",
    private val port: Int = 48302,
    private var modelNameSubstring: String) {

    private lateinit var lightModelClient: LightModelClient

    init {
        GeneratedLanguages.registerAll()
    }

    fun createConnection() {

        // we require a http client with WS support for the connection
        logger.info("Connecting to light model-server at ws://$host:$port/ws")
        this.lightModelClient = LightModelClient.builder().
            host(host).port(port).
            autoFilterNonLoadedNodes().build()

        // the modelQL query
        this.lightModelClient.changeQuery(buildModelQuery {
            // we traverse from the root (a repository)
            root {
                // to all modules
                children("modules") {
                    // selecting the ones that contain our desired model name
                    whereProperty("name").contains(modelNameSubstring)
                    children("models") {
                        // and extract all root nodes, so Rooms and Courses, and their descendants
                        children("rootNodes") {
                            descendants { }
                        }
                    }
                }
            }
        })
        logger.info("Connection successful")
    }

    suspend fun getAllLectures(): List<N_Lecture> {
        // TODO: can we avoid this cast by using generics in runAsyncRead?
        logger.info("Loading all lectures")
        return runAsyncRead(loadLectures) as List<N_Lecture>
    }

    suspend fun getAllRooms(): List<N_Room> {
        logger.info("Loading all rooms")
        return runAsyncRead(loadRooms) as List<N_Room>
    }

    private suspend fun runAsyncRead(givenFunction: (node: N_Repository) -> List<N_BaseConcept>?): List<N_BaseConcept>? {
        var result: List<N_BaseConcept>? = null
        val root = lightModelClient.waitForRootNode()
        if (root != null) {
            lightModelClient.runRead {
                result = givenFunction(root.typed<N_Repository>())
            }
        }
        return result
    }

    private var loadRooms: (N_Repository) -> List<N_Room>? = Any@{ node: N_Repository ->
        return@Any getAllRootNodesOfTypeInRepository<N_Rooms>(node).rooms
    }

    private var loadLectures: (N_Repository) -> List<N_Lecture>? = Any@{ node: N_Repository ->
        return@Any getAllRootNodesOfTypeInRepository<N_Courses>(node).lectures
    }

    private inline fun <reified R> getAllRootNodesOfTypeInRepository(node: N_Repository) =
            node.unwrap().allChildren.filter { it.isValid }.map { it.typed<N_Module>() }.models.rootNodes.filterIsInstance<R>()

    val resolveNodeIdToConcept: suspend (String) -> N_BaseConcept? = Any@{ ref: String ->
        logger.info("Resolving node $ref")
        return@Any lightModelClient.runRead {
            // TODO: investigate if this approach can be improved
            //INodeReferenceSerializer.deserialize(actualRef).resolveNode(lightModelClient.getRootNode()?.getArea())
            lightModelClient.getNodeIfLoaded(ref)?.typed()?.let { it as N_BaseConcept }
        }

    }

    fun <T> runRead(body: () -> T): T {
        return lightModelClient.runRead {
            body()
        }
    }

    fun getArea(): LightModelClient.Area {
        return this.lightModelClient.Area()
    }



    suspend fun updateRoom(newRoom: Rooom){
        var result: N_Room? = null
        val root = lightModelClient.waitForRootNode()
        if (root != null) {

            var decodedReference = URLDecoder.decode(newRoom.roomRef, Charset.defaultCharset())
            val result = resolveNodeIdToConcept(decodedReference) as N_Room
            // test these
            // "the right way"
            //INodeReferenceSerializer.deserialize(actualRef).resolveNode(lightModelClient.getRootNode()?.getArea())

            // warning not performant!
            // root.typed<N_Repository>().descendants(true).ofType<N_Room>()

            lightModelClient.runWrite {
                result.name = newRoom.name
                result.maxPlaces = newRoom.maxPlaces
                result.hasRemoteEquipment = newRoom.hasRemoteEquipment!!
            }
        }
    }

}