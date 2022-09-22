//
//  ListFollowingView.swift
//  iosApp
//
//  Created by Vladislav on 22.09.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct ListFollowingView: View {
    
    @StateObject var usersVM:FollowingViewModel
    @State var refreshData:Bool = true
    
    var body: some View {
        NavigationView{
            List {
                ForEach(usersVM.users, id: \.self) { user in
                    FollowingView(following: user)
                        .onAppear(){
                            if (usersVM.users.last == user){
                                usersVM.fetchNextData()
                            }
                        }
                }
            }
            .navigationTitle("Following")
            .onAppear(){
                usersVM.fetchFollowing()
            }
        }
    }
}
