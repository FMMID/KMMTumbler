//
//  ViewModel.swift
//  iosApp
//
//  Created by Vladislav on 13.09.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Combine
import shared

class ViewModel: ObservableObject {
    
    let sdk:TumblerViewModel
    
    init(sdk:TumblerViewModel){
        self.sdk = sdk
    }
    
    var isLoaderVisible = PassthroughSubject<Bool, Never>()
    var webTitle = PassthroughSubject<String, Never>()
    var webViewNavigationPublisher = PassthroughSubject<WebViewNavigationAction, Never>()
    var authorizationCode = PassthroughSubject<String, Never>()
    var successAuth = PassthroughSubject<Bool,Never>()
}
