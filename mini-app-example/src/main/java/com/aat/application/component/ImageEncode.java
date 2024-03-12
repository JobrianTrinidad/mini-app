/**
 @license MIT
 Copyright 2021-2022 David "F0rce" Dodlek
 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.aat.application.component;

/*-
 * #%L
 * signature-widget
 * %%
 * Copyright (C) 2021 - 2024 David "F0rce" Dodlek
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

/**
 * This class is used to listen to the image-encode event sent by the frontend. It contains the
 * image URI and the MIME-Type.
 *
 * @author David "F0rce" Dodlek
 */
@DomEvent("image-encode")
public class ImageEncode extends ComponentEvent<SignaturePad> {

  private String image;
  private String type;
  private boolean isEmpty;

  public ImageEncode(
      SignaturePad source,
      boolean fromClient,
      @EventData("event.detail.image") String image,
      @EventData("event.detail.type") String type,
      @EventData("event.detail.isEmpty") Boolean isEmpty) {
    super(source, fromClient);
    this.image = image;
    this.type = type;
    this.isEmpty = isEmpty;
  }

  /**
   * Returns the dataUrl of the encoded image.
   *
   * @return {@link String}
   */
  public String getImage() {
    return this.image;
  }

  /**
   * Returns the type, the image has been encoded with.
   *
   * @return {@link String}
   */
  public String getType() {
    return this.type;
  }

  /**
   * Returns if the signature is empty.
   *
   * @return boolean
   */
  public boolean isEmpty() {
    return this.isEmpty;
  }
}
