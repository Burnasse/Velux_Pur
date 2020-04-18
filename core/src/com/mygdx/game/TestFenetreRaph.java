package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.leaf.Wait;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.classesatrier.Entity.EntityObjects;
import com.mygdx.game.classesatrier.Entity.InGameObject;

public class TestFenetreRaph extends ApplicationAdapter {

	public CameraInputController camController;
	PerspectiveCamera cam;
	Environment environment;
	Array<ModelInstance> instances =new Array<ModelInstance>();
	Array<InGameObject> objectsInstances = new Array<InGameObject>();
	ModelBatch modelBatch;
	Model model;
	AssetManager assets;
	boolean loading;


	@Override
	public void create () {
		Bullet.init();
		modelBatch = new ModelBatch();
		assets = new AssetManager();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		/**
		 * gestion de la camera
		 */

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(3f, 7f, 10f);
		cam.lookAt(0, 4f, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
/**
 * on creer les objets ici
 */
		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "ground";
		mb.part("ground", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)))
				.box(5f, 1f, 5f);
		/*mb.node().id = "sphere";
		mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)))
				.sphere(1f, 1f, 1f, 10, 10);*/
		mb.node().id = "box";
		mb.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE)))
				.box(1f, 1f, 1f);
		/*mb.node().id = "cone";
		mb.part("cone", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
				.cone(1f, 2f, 1f, 10);
		mb.node().id = "capsule";
		mb.part("capsule", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.CYAN)))
				.capsule(0.5f, 2f, 10);
		mb.node().id = "cylinder";
		mb.part("cylinder", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.MAGENTA))).cylinder(1f, 2f, 1f, 10);*/
		model = mb.end();

/**
 * on creer entité voulu
 */
		EntityObjects box = new EntityObjects("box","dd",0,0,0);
		/**
		 * on instancie l'objet
		 */
		InGameObject boxInstancie = box.createObjectFromModel("box",model,new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f)));

		/***
		 * on deplace l'objet
		 */
		boxInstancie.transform.trn(0f,2,0f);

		objectsInstances.add(boxInstancie);

	}

	private void doneLoading() {
		Model model = assets.get("block.obj", Model.class);
		ModelInstance block = new ModelInstance(model);
		instances.add(block);
		loading = false;
	}


	@Override
	public void render () {

		camController.update();

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		modelBatch.begin(cam);
		modelBatch.render(objectsInstances, environment);
		modelBatch.end();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
		instances.clear();
		assets.dispose();
	}
}
