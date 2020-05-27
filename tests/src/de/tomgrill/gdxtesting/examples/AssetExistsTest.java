/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.tomgrill.gdxtesting.examples;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class AssetExistsTest {

	@Test
	public void AssetsExist() {
		assertTrue( Gdx.files.internal("../core/assets/PhotoMenuVelux.jpeg").exists());
		assertTrue( Gdx.files.internal("../core/assets/menuAssets/UI.atlas").exists());
		assertTrue( Gdx.files.internal("../core/assets/menuAssets/UI.json").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/idleG3D.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/runningG3D.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/walkG3D.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/jumpG3D.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/dodgeG3D.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/dance/dance.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/dance/chicken.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/dance/macarena.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/dance/shuffle.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/dance/thriller.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/slashs.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/PlayerAssets/bowG3D.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/npc/npc.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/npc/sittingNpc.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/skyBox.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/groundG3D.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/wallG3D.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/ground.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/enemies/enemyG3D.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/enemies/enemyRunG3D.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/enemies/enemyFireG3D.g3db").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/menuTheme.mp3").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/musicLevel.mp3").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/musicVillage.mp3").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/ambianceVillage.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/ambianceDungeon.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/danceMusic/danceMusic.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/danceMusic/chickenMusic.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/danceMusic/macarenaMusic.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/danceMusic/shuffleMusic.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/danceMusic/thrillerMusic.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/attackSound.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/bowSound.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/dodgeSound.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/jumpSound.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/pasRun.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/zombieAttackSound.wav").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/hitSound.mp3").exists());
		assertTrue( Gdx.files.internal("../core/assets/sound/playerHurtSound.wav").exists());
	}
}
