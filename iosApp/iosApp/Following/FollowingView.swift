//
//  FollowingView.swift
//  iosApp
//
//  Created by Vladislav on 21.09.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct FollowingView: View {
 
    var following:UserFollowing
    
    var body: some View {
            HStack(alignment: .center, spacing: nil, content: {
                Text(following.name)
            })
        }
}

