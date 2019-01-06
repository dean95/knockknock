/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.knockknock.board

class BoardManager {

  //Raspberry Pi board
  companion object {
    private const val BUTTON_PIN_NAME = "BCM21"
    private const val RED_LED_PIN_NAME = "BCM6"
    private const val GREEN_LED_PIN_NAME = "BCM19"
    private const val BLUE_LED_PIN_NAME = "BCM26"
  }

  //NXP i.MX7D board
//  companion object {
//    private const val BUTTON_PIN_NAME = "GPIO6_IO14"
//    private const val RED_LED_PIN_NAME = "GPIO2_IO02"
//    private const val GREEN_LED_PIN_NAME = "TODO"
//    private const val BLUE_LED_PIN_NAME = "TODO"
//  }
}